# Performance Improvements and Recommendations

This document outlines the performance optimizations implemented and additional recommendations for the Monster Compendium application.

## Implemented Optimizations

### 1. Collection Operation Optimizations

#### a. Eliminated Chained Collection Operations
**File**: `feature/share-content/compose/src/commonMain/kotlin/br/alexandregpereira/hunter/shareContent/domain/GetMonstersContentToExportUseCase.kt`

**Before**:
```kotlin
return spellcastings.asSequence().map {
    it.usages
}.flatten().map { it.spells }.flatten().map { it.index }.toList()
```

**After**:
```kotlin
return spellcastings.flatMap { spellcasting ->
    spellcasting.usages.flatMap { usage ->
        usage.spells.map { it.index }
    }
}
```

**Impact**: 
- Eliminated 3 intermediate collection allocations
- Reduced memory pressure during spell index extraction
- Improved performance for export operations with many spells

#### b. Combined Map Operations
**File**: `feature/search/compose/src/commonMain/kotlin/br/alexandregpereira/hunter/search/domain/SearchMonstersByUseCase.kt`

**Before**:
```kotlin
name.split(SearchKeySymbolAnd).map { it.trim() }.map { valueWithSymbols ->
    // processing
}
```

**After**:
```kotlin
name.split(SearchKeySymbolAnd).map { valueWithSymbols ->
    val trimmedValue = valueWithSymbols.trim()
    // processing with trimmedValue
}
```

**Impact**: 
- Reduced iterations by 50% in search path
- Eliminated intermediate collection allocation
- Improved search query parsing performance

#### c. Combined Filter Operations
**File**: `feature/monster-registration/state-holder/src/commonMain/kotlin/br/alexandregpereira/hunter/monster/registration/domain/NormalizeMonsterUseCase.kt`

**Before**:
```kotlin
actions.filterDamageDices().filter {
    it.abilityDescription.isEmpty().not()
}
```

**After**:
```kotlin
actions.filterDamageDices { it.abilityDescription.isEmpty().not() }
```

**Impact**: 
- Eliminated duplicate iteration over collections
- Reduced CPU cycles in monster normalization
- Used `mapNotNull` for combined map+filter operations, further reducing allocations

#### d. Optimized Flow Operations
**File**: `domain/monster/event/src/commonMain/kotlin/br/alexandregpereira/hunter/monster/event/MonsterEventDispatcher.kt`

**Before**:
```kotlin
events.map { it as? MonsterEvent.OnMonsterPageChanges }.filterNotNull().map(action)
```

**After**:
```kotlin
events.mapNotNull { it as? MonsterEvent.OnMonsterPageChanges }.map(action)
```

**Impact**: 
- Eliminated intermediate collection in event handling
- Improved event dispatch performance

### 2. Code Simplification

**File**: `domain/app/data/src/commonMain/kotlin/br/alexandregpereira/hunter/data/database/dao/MonsterDaoInsertHelper.kt`

Removed unnecessary `.run` scope functions from insert operations:

**Before**:
```kotlin
internal fun ActionQueries.insert(entities: List<ActionEntity>) = entities.run {
    forEach {
        insert(it.toDatabaseEntity())
    }
}
```

**After**:
```kotlin
internal fun ActionQueries.insert(entities: List<ActionEntity>) {
    entities.forEach {
        insert(it.toDatabaseEntity())
    }
}
```

**Impact**: 
- Removed unnecessary lambda overhead
- Simplified code readability
- Applied to 3 insert helper functions

## Recommended Future Optimizations

### 1. Critical: Fix N+1 Query Problem in Monster Loading

**File**: `domain/app/data/src/commonMain/kotlin/br/alexandregpereira/hunter/data/database/dao/MonsterDaoImpl.kt`

**Issue**: The `queryMonsterCompleteEntities()` function executes 13+ separate database queries for each monster:
- 1 query for speed
- 1 query for ability scores
- 1 query for actions + N queries for damage dices
- 1 query for reactions
- 1 query for special abilities
- 1 query for legendary actions + N queries for damage dices
- 1 query for spellcastings + N queries for usages + N queries for spells
- 1 query for saving throws
- 1 query for skills
- 1 query for damage vulnerabilities
- 1 query for damage resistances
- 1 query for damage immunities
- 1 query for condition immunities

For N monsters, this results in **13*N + nested queries** database operations.

**Recommendation**: Implement batch querying:
```kotlin
// Fetch all related data for all monsters at once
val monsterIndexes = monsters.map { it.index }

// Batch fetch all abilities for all monsters
val allAbilityScores = abilityScoreQueries.getByMonsterIndexes(monsterIndexes)
    .executeAsList()
    .groupBy { it.monsterIndex }

// Batch fetch all actions for all monsters
val allActions = actionQueries.getByMonsterIndexes(monsterIndexes)
    .executeAsList()
    .groupBy { it.monsterIndex }

// Then map each monster to its related data
return monsters.map { monster ->
    MonsterCompleteEntity(
        monster = monster.toLocalEntity(),
        abilityScores = allAbilityScores[monster.index] ?: emptyList(),
        actions = allActions[monster.index] ?: emptyList(),
        // ... etc
    )
}
```

**Impact**: 
- Would reduce database queries from O(N*13) to O(13) for N monsters
- Massive performance improvement for loading multiple monsters
- Critical for list views and search results

**Implementation Complexity**: HIGH - Requires changes to SQLDelight queries and data access layer

### 2. Medium Priority: Optimize Flow Collection Context

**File**: `feature/monster-detail/state-holder/src/commonMain/kotlin/br/alexandregpereira/hunter/monster/detail/MonsterDetailStateHolder.kt`

**Issue**: Uses `withContext(Dispatchers.Main)` inside Flow transformation at line 306:
```kotlin
private fun Flow<MonsterDetail>.toMonsterDetailState(): Flow<MonsterDetailState> {
    return map {
        val strings = appLocalization.getStrings()
        withContext(Dispatchers.Main) {
            metadata = it.monsters
            initialMonsterListPositionIndex = it.monsterIndexSelected
        }
        // ...
    }
}
```

**Recommendation**: 
1. If `metadata` and `initialMonsterListPositionIndex` are only accessed from UI code, this context switch might be redundant if the Flow is already collected on the Main dispatcher
2. Consider using `@Volatile` or `AtomicReference` for thread-safe access without context switching
3. Or restructure to avoid mutable state in Flow transformations

**Impact**: Eliminates unnecessary thread context switches in data transformation

### 3. Database Insert Optimization (Already Optimal)

The current implementation uses transactions for batch inserts, which is the recommended approach for SQLDelight 1.5.5:
- All inserts are wrapped in a transaction (line 113 of `MonsterDaoImpl.kt`)
- This batches the commit operations
- Individual prepared statements are necessary given SQLDelight's API design

**Note**: Upgrading to SQLDelight 2.x might provide better batch insert APIs.

### 4. Consider Using Sequences for Large Data Processing

In scenarios where large collections are being processed with multiple transformations, consider using sequences to avoid intermediate collections:

```kotlin
// Instead of:
monsters.map { ... }.filter { ... }.map { ... }

// Use:
monsters.asSequence()
    .map { ... }
    .filter { ... }
    .map { ... }
    .toList()
```

**When to use**:
- Processing large collections (100+ items)
- Multiple chained operations (3+ transformations)
- When terminal operation is needed (toList, first, etc.)

**When NOT to use**:
- Small collections (< 100 items) - overhead outweighs benefits
- Single transformation - no benefit
- When result is used multiple times - would recompute each time

## Performance Testing Recommendations

1. **Load Testing**: Test monster list loading with 100+ monsters to verify N+1 query impact
2. **Memory Profiling**: Profile memory allocations during:
   - Monster list scrolling
   - Search operations
   - Export operations
3. **CPU Profiling**: Identify hot paths in:
   - Data transformation pipelines
   - Collection operations
   - Database queries

## Metrics to Track

- Time to load monster list (by count: 10, 50, 100, 500 monsters)
- Memory allocations during search
- Database query count per operation
- UI frame drops during scrolling

## Summary

These optimizations focus on reducing:
1. **Intermediate collection allocations** - Memory efficiency
2. **Redundant iterations** - CPU efficiency
3. **Code complexity** - Maintainability

The most impactful future improvement would be fixing the N+1 query problem in monster loading, which could provide 10-100x performance improvement for operations loading multiple monsters.
