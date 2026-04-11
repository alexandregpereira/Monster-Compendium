//
//  IdentifyEvent.swift
//
//
//  Created by Marvin Liu on 11/3/22.
//

import Foundation

public class IdentifyEvent: BaseEvent {
    override public var eventType: String {
        get {
            return "$identify"
        }
        set {
        }
    }

    convenience init() {
        self.init(eventType: "$identify")
    }
}
