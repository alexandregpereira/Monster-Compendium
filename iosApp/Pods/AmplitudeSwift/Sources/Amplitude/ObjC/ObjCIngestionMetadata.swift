import Foundation

@objc(AMPIngestionMetadata)
public class ObjCIngestionMetadata: NSObject {
    internal var ingestionMetadata: IngestionMetadata

    @objc
    convenience public override init() {
        self.init(IngestionMetadata())
    }

    internal init(_ ingestionMetadata: IngestionMetadata) {
        self.ingestionMetadata = ingestionMetadata
    }

    @objc
    public var sourceName: String? {
        get {
            ingestionMetadata.sourceName
        }
        set(value) {
            ingestionMetadata.sourceName = value
        }
    }

    @objc
    public var sourceVersion: String? {
        get {
            ingestionMetadata.sourceVersion
        }
        set(value) {
            ingestionMetadata.sourceVersion = value
        }
    }
}
