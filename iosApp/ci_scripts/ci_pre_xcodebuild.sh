#!/bin/sh
set -e

cd $CI_WORKSPACE/iosApp
brew install cocoapods
pod install
