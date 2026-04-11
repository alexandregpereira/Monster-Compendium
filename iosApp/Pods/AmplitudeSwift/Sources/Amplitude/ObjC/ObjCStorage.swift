import Foundation

@objc(AMPStorage)
public class ObjCStorage: NSObject {
    private weak var amplitude: Amplitude?

    internal init(amplitude: Amplitude) {
        self.amplitude = amplitude
    }

    @objc
    public func getEventsStrings() -> [String] {
        guard let storage = amplitude?.storage else { return [] }
        return getEventsStrings(storage: storage)
    }

    @objc
    public func getInterceptedIdentifiesStrings() -> [String] {
        guard let storage = amplitude?.identifyStorage else { return [] }
        return getEventsStrings(storage: storage)
    }

    private func getEventsStrings(storage: Storage) -> [String] {
        guard let eventURLs: [URL] = storage.read(key: StorageKey.EVENTS) else { return [] }
        return eventURLs.map { eventURL in
            storage.getEventsString(eventBlock: eventURL)
        }.compactMap { $0 }
    }
}
