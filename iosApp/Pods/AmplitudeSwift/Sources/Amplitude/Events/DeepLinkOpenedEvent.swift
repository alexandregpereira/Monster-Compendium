import Foundation

public class DeepLinkOpenedEvent: BaseEvent {
    override public var eventType: String {
        get {
            Constants.AMP_DEEP_LINK_OPENED_EVENT
        }
        set {
        }
    }

    public convenience init(url: URL) {
        self.init(url: url.absoluteString)
    }

    public convenience init(url: NSURL) {
        self.init(url: url.absoluteString)
    }

    public convenience init(activity: NSUserActivity) {
        let url = activity.webpageURL?.absoluteString
        var referrer: String?
        if #available(iOS 11, tvOS 11.0, macOS 10.13, watchOS 4.0, *) {
            referrer = activity.referrerURL?.absoluteString
        }
        self.init(url: url, referrer: referrer)
    }

    public init(url: String?, referrer: String? = nil) {
        var eventProperties = [
            Constants.AMP_APP_LINK_URL_PROPERTY: url ?? "",
        ]
        if let referrer = referrer {
            eventProperties[Constants.AMP_APP_LINK_REFERRER_PROPERTY] = referrer
        }
        super.init(eventType: Constants.AMP_DEEP_LINK_OPENED_EVENT, eventProperties: eventProperties)
    }

    required public init(from decoder: Decoder) throws {
        try super.init(from: decoder)
    }
}
