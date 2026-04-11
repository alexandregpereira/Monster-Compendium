import Foundation

@objc(AMPScreenViewedEvent)
public class ObjCScreenViewedEvent: ObjCBaseEvent {
    @objc(initWithScreenName:)
    public static func initWithScreenName(screenName: String) -> ObjCScreenViewedEvent {
        ObjCScreenViewedEvent(screenName: screenName)
    }

    @objc(initWithScreenName:)
    public convenience init(screenName: String) {
        self.init(event: ScreenViewedEvent(screenName: screenName))
    }
}
