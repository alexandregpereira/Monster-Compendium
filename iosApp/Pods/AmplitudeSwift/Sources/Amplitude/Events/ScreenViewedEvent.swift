import Foundation

public class ScreenViewedEvent: BaseEvent {
    override public var eventType: String {
        get {
            Constants.AMP_SCREEN_VIEWED_EVENT
        }
        set {
        }
    }

    public init(screenName: String) {
        super.init(eventType: Constants.AMP_SCREEN_VIEWED_EVENT, eventProperties: [
            Constants.AMP_APP_SCREEN_NAME_PROPERTY: screenName
        ])
    }

    required public init(from decoder: Decoder) throws {
        try super.init(from: decoder)
    }
}
