import Foundation
import AnalyticsConnector

class AnalyticsConnectorIdentityPlugin: ObservePlugin {
    private var connector: AnalyticsConnector?

    override func setup(amplitude: Amplitude) {
        super.setup(amplitude: amplitude)
        connector = AnalyticsConnector.getInstance(amplitude.configuration.instanceName)
        connector?.identityStore.editIdentity()
            .setUserId(amplitude.getUserId())
            .setDeviceId(amplitude.getDeviceId())
            .commit()
    }

    override func onUserIdChanged(_ userId: String?) {
        connector?.identityStore.editIdentity().setUserId(userId).commit()
    }

    override func onDeviceIdChanged(_ deviceId: String?) {
        connector?.identityStore.editIdentity().setDeviceId(deviceId).commit()
    }
}
