/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.data.di

import br.alexandregpereira.hunter.data.Database
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import org.koin.core.scope.Scope
import java.io.File

internal actual fun Scope.createSqlDriver(): SqlDriver {
    val databaseFile = getDatabaseFile()
    val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:${databaseFile.absolutePath}")

    if (databaseFile.exists()) {
        val currentVersion = driver.getDatabaseVersion()
        val schemaVersion = Database.Schema.version
        if (schemaVersion > currentVersion) {
            Database.Schema.migrate(driver, currentVersion, schemaVersion)
            driver.setDatabaseVersion(schemaVersion)
            println("init: migrated from $currentVersion to $schemaVersion")
        } else {
            Database.Schema.create(driver)
        }
    } else {
        driver.setDatabaseVersion(Database.Schema.version)
        Database.Schema.create(driver)
    }

    return driver
}

private fun getDatabaseFile(): File {
    val userFolder = System.getenv("XDG_DATA_HOME")
        ?: "${System.getProperty("user.home")}/.local/share"
    val appDataFolder = File(userFolder, ".monster-compendium")
    if (appDataFolder.exists().not()) {
        appDataFolder.mkdirs()
    }
    return File(appDataFolder, "hunter-database.db")
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
