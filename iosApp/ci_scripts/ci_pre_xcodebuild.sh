#!/bin/sh
set -e

cd "$(dirname "$0")/.."
brew install cocoapods
pod install
