//
//  ObjCInteractionsOptions.swift
//  Amplitude-Swift
//
//  Created by Jin Xu on 6/27/25.
//

import Foundation

@objc(AMPInteractionsOptions)
public class ObjCInteractionsOptions: NSObject {
    let options: InteractionsOptions

    @objc
    public convenience init(rageClick: ObjCRageClickOptions = .init(enabled: true),
                            deadClick: ObjCDeadClickOptions = .init(enabled: true)) {
        self.init(InteractionsOptions(rageClick: rageClick.options, deadClick: deadClick.options))
    }

    init(_ options: InteractionsOptions) {
        self.options = options
        super.init()
    }
}

@objc(AMPRageClickOptions)
public class ObjCRageClickOptions: NSObject {
    let options: RageClickOptions

    @objc
    public convenience init(enabled: Bool = true) {
        self.init(RageClickOptions(enabled: enabled))
    }

    init(_ options: RageClickOptions) {
        self.options = options
        super.init()
    }

    @objc public var enabled: Bool {
        return options.enabled
    }
}

@objc(AMPDeadClickOptions)
public class ObjCDeadClickOptions: NSObject {
    let options: DeadClickOptions

    @objc
    public convenience init(enabled: Bool = true) {
        self.init(DeadClickOptions(enabled: enabled))
    }

    init(_ options: DeadClickOptions) {
        self.options = options
        super.init()
    }

    @objc public var enabled: Bool {
        return options.enabled
    }
}
