/*
 * Copyright 2023 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.data.di

import br.alexandregpereira.hunter.data.Database
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import org.koin.core.scope.Scope
import java.io.File

internal actual fun Scope.createSqlDriver(): SqlDriver {
    val databasePath = getDatabasePath()
    val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:$databasePath")

    val currentVersion = driver.getDatabaseVersion()
    val schemaVersion = Database.Schema.version
    if (schemaVersion > currentVersion) {
        Database.Schema.migrate(driver, currentVersion, schemaVersion)
        driver.setDatabaseVersion(schemaVersion)
        println("init: migrated from $currentVersion to $schemaVersion")
    } else {
        Database.Schema.create(driver)
    }

    return driver
}

private fun getDatabasePath(): String {
    val userFolder = System.getProperty("user.home")
    val appDataFolder = File(userFolder, ".monster-compendium")
    if (appDataFolder.exists().not()) {
        appDataFolder.mkdirs()
    }
    val databasePath = File(appDataFolder, "hunter-database.db")
    return databasePath.absolutePath
}

private fun SqlDriver.getDatabaseVersion(): Int {
    val sqlCursor = this.executeQuery(
        identifier = null,
        sql = "PRAGMA user_version;",
        parameters = 0,
        binders = null
    )

    val initialDatabaseVersion = 25
    return sqlCursor.getLong(0)?.toInt()
        ?.takeUnless { it == 0 } ?: initialDatabaseVersion
}

private fun SqlDriver.setDatabaseVersion(version: Int) {
    this.execute(null, "PRAGMA user_version = $version;", 0, null)
}
