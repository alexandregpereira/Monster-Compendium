//
//  NetworkConnectivityCheckerPlugin.swift
//  Amplitude-Swift
//
//  Created by Xinyi.Ye on 1/26/24.
//

import Foundation
import Network
import Combine

// Define a custom struct to represent network path status
public struct NetworkPath {
    public var status: NWPath.Status

    public init(status: NWPath.Status) {
        self.status = status
    }
}

// Protocol for creating network paths
protocol PathCreationProtocol {
    var networkPathPublisher: AnyPublisher<NetworkPath, Never>? { get }
    func start(queue: DispatchQueue)
}

// Implementation of PathCreationProtocol using NWPathMonitor
final class PathCreation: PathCreationProtocol {
    public var networkPathPublisher: AnyPublisher<NetworkPath, Never>?
    private let subject = PassthroughSubject<NWPath, Never>()
    private let monitor = NWPathMonitor()

    func start(queue: DispatchQueue) {
        monitor.pathUpdateHandler = subject.send
        networkPathPublisher = subject
            .map { NetworkPath(status: $0.status) }
            .eraseToAnyPublisher()
        monitor.start(queue: queue)
    }
}

open class NetworkConnectivityCheckerPlugin: BeforePlugin {
    public static let Disabled: Bool? = nil
    var pathCreation: PathCreationProtocol
    private var pathUpdateCancellable: AnyCancellable?

    init(pathCreation: PathCreationProtocol = PathCreation()) {
        self.pathCreation = pathCreation
        super.init()
    }

    open override func setup(amplitude: Amplitude) {
        super.setup(amplitude: amplitude)
        amplitude.logger?.debug(message: "Installing NetworkConnectivityCheckerPlugin, offline feature should be supported.")

        pathCreation.start(queue: amplitude.trackingQueue)
        let logger = amplitude.logger
        pathUpdateCancellable = pathCreation.networkPathPublisher?
            .sink(receiveValue: { [weak amplitude, logger] networkPath in
                guard let amplitude = amplitude else {
                    logger?.debug(message: "Received network connectivity updated when amplitude instance has been deallocated")
                    return
                }
                let isOffline = !(networkPath.status == .satisfied)
                if amplitude.configuration.offline == isOffline {
                    return
                }
                amplitude.logger?.debug(message: "Network connectivity changed to \(isOffline ? "offline" : "online").")
                amplitude.configuration.offline = isOffline
                if !isOffline {
                    amplitude.flush()
                }
            })
    }

    open override func teardown() {
        pathUpdateCancellable?.cancel()
    }
}
