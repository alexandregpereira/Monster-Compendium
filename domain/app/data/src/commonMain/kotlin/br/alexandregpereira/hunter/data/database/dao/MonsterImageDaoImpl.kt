package br.alexandregpereira.hunter.data.database.dao

import br.alexandregpereira.hunter.data.monster.local.dao.MonsterImageDao
import br.alexandregpereira.hunter.data.monster.local.entity.MonsterImageEntity
import br.alexandregpereira.hunter.database.MonsterImageQueries
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import br.alexandregpereira.hunter.database.MonsterImageEntity as MonsterImageDatabaseEntity

internal class MonsterImageDaoImpl(
    private val monsterImageQueries: MonsterImageQueries,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : MonsterImageDao {

    private val mutex = Mutex()

    override suspend fun getMonsterImages(): List<MonsterImageEntity> = withContext(dispatcher) {
        monsterImageQueries.getMonsterImages().executeAsList().map {
            it.toLocalEntity()
        }
    }

    override suspend fun getMonsterImage(monsterIndex: String): MonsterImageEntity? {
        return monsterImageQueries.getMonsterImage(monsterIndex)
            .executeAsOneOrNull()
            ?.toLocalEntity()
    }

    override suspend fun insert(
        monsterImages: List<MonsterImageEntity>
    ) = mutex.withLock {
        withContext(dispatcher) {
            monsterImageQueries.transaction {
                monsterImages.forEach { monsterImage ->
                    monsterImageQueries.insert(monsterImage.toDatabaseEntity())
                }
            }
        }
    }

    override suspend fun deleteMonsterImage(monsterIndex: String) {
        withContext(dispatcher) {
            monsterImageQueries.delete(monsterIndex)
        }
    }

    override suspend fun deleteMonsterImages(monsterIndexes: List<String>) {
        withContext(dispatcher) {
            monsterImageQueries.deleteByIndexes(monsterIndexes)
        }
    }

    private fun MonsterImageEntity.toDatabaseEntity(): MonsterImageDatabaseEntity {
        return MonsterImageDatabaseEntity(
            monsterIndex = monsterIndex,
            backgroundColorLight = backgroundColorLight,
            backgroundColorDark = backgroundColorDark,
            isHorizontalImage = isHorizontalImage?.let {
                if (it) 1 else 0
            },
            imageContentScale = imageContentScale?.toLong(),
            imageUrl = imageUrl,
        )
    }

    private fun MonsterImageDatabaseEntity.toLocalEntity(): MonsterImageEntity {
        return MonsterImageEntity(
            monsterIndex = monsterIndex,
            backgroundColorLight = backgroundColorLight,
            backgroundColorDark = backgroundColorDark,
            isHorizontalImage = isHorizontalImage?.let {
                it == 1L
            },
            imageContentScale = imageContentScale?.toInt(),
            imageUrl = imageUrl,
        )
    }
}
