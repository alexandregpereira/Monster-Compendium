package br.alexandregpereira.hunter.app

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import br.alexandregpereira.hunter.data.Database
import br.alexandregpereira.hunter.localization.Language
import com.russhwolf.settings.MapSettings
import com.russhwolf.settings.Settings
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import org.koin.core.context.startKoin
import org.koin.dsl.module

internal class KoinTestRunner : AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, KoinTestApplication::class.qualifiedName, context)
    }
}

internal class KoinTestApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            initAndroidModules(this@KoinTestApplication)
            allowOverride(true)
            module {
                factory<SqlDriver> {
                    AndroidSqliteDriver(
                        schema = Database.Schema,
                        context = get<Application>(),
                        name = null
                    )
                }
                single<Settings> { MapSettings() }
                factory<Language> { Language.ENGLISH }
            }.also {
                modules(it)
            }
        }
    }
}
