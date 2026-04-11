import Foundation

@objc(AMPDeepLinkOpenedEvent)
public class ObjCDeepLinkOpenedEvent: ObjCBaseEvent {
    @objc(initWithActivity:)
    public static func initWithActivity(activity: NSUserActivity) -> ObjCDeepLinkOpenedEvent {
        ObjCDeepLinkOpenedEvent(activity: activity)
    }

    @objc(initWithUrl:)
    public static func initWithUrl(url: String?) -> ObjCDeepLinkOpenedEvent {
        ObjCDeepLinkOpenedEvent(url: url)
    }

    @objc(initWithUrl:referrer:)
    public static func initWithUrl(url: String?, referrer: String?) -> ObjCDeepLinkOpenedEvent {
        ObjCDeepLinkOpenedEvent(url: url, referrer: referrer)
    }

    @objc(initWithActivity:)
    public convenience init(activity: NSUserActivity) {
        self.init(event: DeepLinkOpenedEvent(activity: activity))
    }

    @objc(initWihUrl:)
    public convenience init(url: String?) {
        self.init(url: url, referrer: nil)
    }

    @objc(initWihUrl:referrer:)
    public convenience init(url: String?, referrer: String?) {
        self.init(event: DeepLinkOpenedEvent(url: url, referrer: referrer))
    }

    internal init(event: DeepLinkOpenedEvent) {
        super.init(event: event)
    }
}
