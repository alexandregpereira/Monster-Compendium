#!/bin/sh
set -e

# Install Temurin 17 for Gradle (required by the KMP embedAndSignAppleFrameworkForXcode build phase)
ARCH=$(uname -m)
if [ "$ARCH" = "arm64" ]; then
    JDK_ARCH="aarch64"
else
    JDK_ARCH="x64"
fi
curl -L "https://api.adoptium.net/v3/binary/latest/17/ga/mac/${JDK_ARCH}/jdk/hotspot/normal/eclipse" -o /tmp/temurin17.tar.gz
mkdir -p ~/Library/Java/JavaVirtualMachines
tar xzf /tmp/temurin17.tar.gz -C ~/Library/Java/JavaVirtualMachines/

# Install CocoaPods and fetch dependencies
cd "$(dirname "$0")/.."
brew install cocoapods
pod install
