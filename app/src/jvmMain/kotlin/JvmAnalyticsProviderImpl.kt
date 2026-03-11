import br.alexandregpereira.hunter.analytics.JvmAnalyticsProvider
import br.alexandregpereira.hunter.app.AppConfig
import org.koin.dsl.module

internal val jvmAnalyticsModule = module {
    factory<JvmAnalyticsProvider> {
        JvmAnalyticsProviderImpl()
    }
}

internal class JvmAnalyticsProviderImpl : JvmAnalyticsProvider {
    override fun getVersionName(): String = AppConfig.VERSION_NAME
}
