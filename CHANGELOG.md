# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/), and this project adheres to [Semantic Versioning](https://semver.org/).


## [2.0.0] - 2026-03-28

### Changed
- Upgraded to Kotlin 2.3.20, Gradle 9.4.1, JDK 21+.
- Upgraded to fluid-gradle 3.0.0.
- Upgraded KotlinPoet to 2.2.0.
- Upgraded Ktor to 3.4.2.
- Upgraded fluid-meta to 0.15.0 and fluid-compiler to 0.15.0.
- Merged `coding-jdk8` module into `coding` — java.time codecs are now included directly.

### Removed
- Removed `ktor-client` module — use `ktor-serialization` instead.
- Removed `coding-jdk8` module — codecs moved to `coding`.
- Removed dynamic classloading for java.time codec registration.


## [1.5.0]

### Changed
- Updated all dependencies.


## [1.4.0]

### Changed
- Updated all dependencies.
