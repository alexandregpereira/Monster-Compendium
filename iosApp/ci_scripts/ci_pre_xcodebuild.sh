#!/bin/sh
set -e

# Install Java for Gradle (required by the KMP embedAndSignAppleFrameworkForXcode build phase)
brew install --cask temurin@17

# Install CocoaPods and fetch dependencies
cd "$(dirname "$0")/.."
brew install cocoapods
pod install
