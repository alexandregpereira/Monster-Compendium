//
//  InMemoryStorage.swift
//
//
//  Created by Marvin Liu on 10/28/22.
//

import Foundation

class InMemoryStorage: Storage {
    typealias EventBlock = URL

    func write(key: StorageKey, value: Any?) {

    }

    func read<T>(key: StorageKey) -> T? {
        return nil
    }

    func reset() {

    }

    func rollover() {

    }

    func getEventsString(eventBlock: EventBlock) -> String? {
        return nil
    }

    func remove(eventBlock: EventBlock) {

    }

    func splitBlock(eventBlock: EventBlock, events: [BaseEvent]) {

    }

    func getResponseHandler(
        configuration: Configuration,
        eventPipeline: EventPipeline,
        eventBlock: EventBlock,
        eventsString: String
    ) -> ResponseHandler {
        abort()
    }
}
