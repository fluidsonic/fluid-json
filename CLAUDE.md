# fluid-json

## Project

Kotlin multiplatform JSON library with codec system, annotation processor, and Ktor integration.

## Build

- Kotlin 2.3.20 (JVM)
- Gradle 9.4.1
- fluid-gradle 3.0.0 plugin
- JDK 21+

```sh
./gradlew build        # build all modules
./gradlew jvmTest      # run tests
```

## Modules

| Module | Description |
|---|---|
| annotations | @Json annotation definitions |
| basic | Low-level JSON parsing/serialization |
| coding | Mid-level codec framework (includes java.time codecs) |
| annotation-processor | KAPT-based code generation for @Json |
| ktor-serialization | Ktor content negotiation integration |
| examples | Usage examples |

## Conventions

- Tags have no `v` prefix (e.g. `2.0.0`)
- Tab indentation in Kotlin/Gradle files
- Branch: main
- Test framework: kotlin.test + JUnit
