# Monster Compendium - D&D 5th Edition Bestiary a

<p align="center" >
    <img src="media/icon-monster-compendium.png" width ="40%"  align="center"/>
</p>

[![CI/CD](https://github.com/alexandregpereira/hunter/actions/workflows/Main.yml/badge.svg?branch=main)](https://github.com/alexandregpereira/hunter/actions/workflows/Main.yml) <a href="https://devlibrary.withgoogle.com/products/android/repos/alexandregpereira-monster-compendium" target="_blank"><img alt="Android Dev Library" src="https://img.shields.io/badge/Google%20Dev%20Library-alexandregpereira-blue?style=flat&logo=android"/></a>

Monsters Compendium is an open-source Kotlin Multiplatform application for Android, iOS, Mac, Windows and Linux, offering extensive information on monsters from the Dungeons & Dragons 5th edition role-playing game. The default content is derived from the SRD (System Reference Document), providing a solid foundation for users. Furthermore, the app supports the addition of new custom content, enhancing its versatility and adaptability for players and game masters alike.

<a href='https://play.google.com/store/apps/details?id=br.alexandregpereira.hunter.app&hl=en_US&gl=US&pcampaignid=pcampaignidMKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img width="20%" alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png'/></a>

## Preview

<p align="center">
    <img src="media/monster-compendium.gif" width ="32%" />
    <img src="media/monster-detail-pager.gif" width ="32%" />
    <img src="media/monster-detail.gif" width ="32%" />
</p>

## Key features:

- A well-organized monster compendium, divided into intuitive sections for easy navigation, complete with visually appealing images for an engaging user experience.
- Comprehensive monster details, featuring stat blocks, monster lore, captivating high-resolution images, and links to related spell details. Users can navigate through the compendium pages seamlessly, simulating the feel of paging through a physical book.
- Quick access to spell details of the monsters, providing essential information at your fingertips.
- A powerful search functionality that allows users to quickly find specific monsters.
- A user-friendly interface to organize monsters into customizable folders for better campaign management.

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

- [Jetpack Compose](https://developer.android.com/jetpack/compose): Android’s modern toolkit for building native UI. It simplifies and accelerates UI development on Android.
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-guide.html): A coroutine is a concurrency design pattern that you can use on Android to simplify code that executes asynchronously.
- [Kotlin Flow](https://kotlinlang.org/docs/flow.html): Stream of data that can be computed asynchronously. Built in top of the Kotlin Coroutines.
- [Koin](https://github.com/InsertKoinIO/koin): A pragmatic lightweight dependency injection framework for Kotlin & Kotlin Multiplatform.
- [SQLDelight](https://github.com/cashapp/sqldelight): A multiplatform SQLite library that generates Kotlin typesafe APIs from SQL statements.
- [Ktor-client](https://github.com/ktorio/ktor): A multiplatform asynchronous HTTP client for Kotlin, providing a clean and extensible API for making network requests.
- [Multiplatform Settings](https://github.com/russhwolf/multiplatform-settings): A library that provides a simple and consistent API for persisting key-value data across iOS, Android, and JVM platforms.
- [Coil Compose](https://coil-kt.github.io/coil/compose): An image loading library for Android backed by Kotlin Coroutines and Jetpack Compose.
- [Kotlin Serialization](https://kotlinlang.org/docs/serialization.html): A library for parsing and serializing JSON data, seamlessly converting API responses into Kotlin objects.

## App Architecture

The app's architecture is based on the Model-View-Intent (MVI) pattern with Clean Architecture principles, implemented in a multi-module project, as illustrated in the image below:

![image](https://github.com/user-attachments/assets/f3aca4fb-5229-448d-985f-686b7490c333)

### Components Responsibilities

- **UI**: It is responsible for displaying data from the UI State to the user and handling user interactions.
- **StateHolder**: It holds and send the UI State, manages the app's UI state and logic, and dispatches changes when receiving intents, enabling a reactive UI experience.
- **UseCases**: It contains the business logic, retrieving data from the Repository interface or coordinating with other use cases to perform complex tasks.
- **Repository**:  It acts as a mediator between different data sources (network or database) and converts the data models into domain models, ensuring a clean separation of concerns.
- **DataSources**: It is responsible for fetching and transferring data from a single source, such as an API or a local database, and returning the data in a consistent format.

## API

Currently, there is no dedicated backend for the app. The app retrieves the data from static JSON files stored [here](https://github.com/alexandregpereira/hunter-api). The JSON were formatted from the API https://dnd5eapi.co.

## Content License

The content in this app is licensed under the Open-Gaming License (OGL). The content and license can found at the [D&D 5th Systems Reference Document (SRD)](https://dnd.wizards.com/resources/systems-reference-document). Dungeons & Dragons (D&D) is a trademark of Wizards of the Coast company.

## Icons License

The icons used in this app have a free license. They are designed by [macrovector from Freepik](https://www.freepik.com/macrovector), [Freepik from Flaticon](https://www.flaticon.com/authors/freepik) and [Material Design from Google](https://github.com/google/material-design-icons/blob/master/LICENSE).

## License

    Copyright 2024 Alexandre Gomes Pereira
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
           http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
