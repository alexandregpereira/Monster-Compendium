//
//  RevenueEvent.swift
//
//
//  Created by Marvin Liu on 11/3/22.
//

import Foundation

public class RevenueEvent: BaseEvent {
    override public var eventType: String {
        get {
            return "revenue_amount"
        }
        set {
        }
    }

    convenience init() {
        self.init(eventType: "revenue_amount")
    }
}
