# Monster Compendium - D&D 5th Edition Bestiary

<p align="center" >
    <img src="media/icon-monster-compendium.png" width ="40%"  align="center"/>
</p>

[![CI/CD](https://github.com/alexandregpereira/hunter/actions/workflows/Main.yml/badge.svg?branch=main)](https://github.com/alexandregpereira/hunter/actions/workflows/Main.yml)

Monsters Compendium is an free and open-source Kotlin Multiplatform application for Android, iOS, Mac, Windows and Linux, offering extensive information on monsters from the Dungeons & Dragons 5th edition role-playing game. The default content is derived from the SRD (System Reference Document), providing a solid foundation for users. Furthermore, the app supports the addition of new custom content, enhancing its versatility and adaptability for players and game masters alike.

## Download

### Android
<a href='https://play.google.com/store/apps/details?id=br.alexandregpereira.hunter.app&hl=en_US&gl=US&pcampaignid=pcampaignidMKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img width="20%" alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png'/></a>

### Desktop
You can download [here](https://github.com/alexandregpereira/Monster-Compendium/releases) the desktop version for Window, Mac or Linux.

## Preview

<p align="center">
    <img src="media/monster-compendium.gif" width ="32%" />
    <img src="media/monster-detail-pager.gif" width ="32%" />
    <img src="media/monster-detail.gif" width ="32%" />
</p>

## Runnning
### Desktop
Run the following command on the root project directory.
```gradlew
./gradlew app:jvmRun -DmainClass=MainKt --quiet
```
### Mobile
You can run the app using [Android Studio](https://developer.android.com/studio?_gl=1*1pkmy4x*_up*MQ..&gclid=Cj0KCQjwwO20BhCJARIsAAnTIVQceG8rGFe0Y8EdcMPTFhZV4VUSSj2ugNkUxKFyFpnTttvY7EljITAaAv6WEALw_wcB&gclsrc=aw.ds) for Android and [xCode](https://developer.apple.com/xcode/) for iOS.

## Tech Stack

The following is an overview of the key technologies and libraries used in this Kotlin Multiplatform project, each with a brief description and a URL for further information:

- [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform): Declarative framework for sharing UIs across multiple platforms. Based on Kotlin and [Jetpack Compose](https://developer.android.com/jetpack/compose).
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-guide.html): A coroutine is a concurrency design pattern that you can use on Android to simplify code that executes asynchronously.
- [Kotlin Flow](https://kotlinlang.org/docs/flow.html): Stream of data that can be computed asynchronously. Built in top of the Kotlin Coroutines.
- [Koin](https://github.com/InsertKoinIO/koin): A pragmatic lightweight dependency injection framework for Kotlin & Kotlin Multiplatform.
- [SQLDelight](https://github.com/cashapp/sqldelight): A multiplatform SQLite library that generates Kotlin typesafe APIs from SQL statements.
- [Ktor-client](https://github.com/ktorio/ktor): A multiplatform asynchronous HTTP client for Kotlin, providing a clean and extensible API for making network requests.
- [Multiplatform Settings](https://github.com/russhwolf/multiplatform-settings): A library that provides a simple and consistent API for persisting key-value data across iOS, Android, and JVM platforms.
- [Coil Compose](https://coil-kt.github.io/coil/compose): An image loading library for Android backed by Kotlin Coroutines and Jetpack Compose.

## API

Currently, there is no dedicated backend for the app. The app retrieves the data from static JSON files stored [here](https://github.com/alexandregpereira/hunter-api). The JSON were formatted from the API https://dnd5eapi.co.

## Content License

The content in this app is licensed under the Open-Gaming License (OGL). The content and license can found at the [D&D 5th Systems Reference Document (SRD)](https://dnd.wizards.com/resources/systems-reference-document). Dungeons & Dragons (D&D) is a trademark of Wizards of the Coast company.

## Icons License

The icons used in this app have a free license. They are designed by [macrovector from Freepik](https://www.freepik.com/macrovector), [Freepik from Flaticon](https://www.flaticon.com/authors/freepik) and [Material Design from Google](https://github.com/google/material-design-icons/blob/master/LICENSE).

## License

    DnD 5th edition monster compendium app
    Copyright (C) 2025 Alexandre Gomes Pereira

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
