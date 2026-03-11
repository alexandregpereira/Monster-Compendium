import br.alexandregpereira.hunter.analytics.JvmAnalyticsProvider
import br.alexandregpereira.hunter.app.AppConfig

internal class JvmAnalyticsProviderImpl : JvmAnalyticsProvider {
    override fun getVersionName(): String = AppConfig.VERSION_NAME
}
