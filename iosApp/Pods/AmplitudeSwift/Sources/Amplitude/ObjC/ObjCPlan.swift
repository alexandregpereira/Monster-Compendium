import Foundation

@objc(AMPPlan)
public class ObjCPlan: NSObject {
    internal var plan: Plan

    @objc
    convenience public override init() {
        self.init(Plan())
    }

    internal init(_ plan: Plan) {
        self.plan = plan
    }

    @objc
    public var branch: String? {
        get {
            plan.branch
        }
        set(value) {
            plan.branch = value
        }
    }

    @objc
    public var source: String? {
        get {
            plan.source
        }
        set(value) {
            plan.source = value
        }
    }

    @objc
    public var version: String? {
        get {
            plan.version
        }
        set(value) {
            plan.version = value
        }
    }

    @objc
    public var versionId: String? {
        get {
            plan.versionId
        }
        set(value) {
            plan.versionId = value
        }
    }
}
