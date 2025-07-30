# Room Ktlint Plugin

A Ktlint rule for formatting SQL queries in Room annotations using [sql-formatter](https://github.com/vertical-blank/sql-formatter).

## Features
- Automatically formats SQL queries in `@Query` annotations in your Kotlin code.
- Integrates with Ktlint and supports autocorrection.
- Customizes SQL formatting for Room/SQLite dialects.

## Usage

### Add the Plugin
Add the plugin to ktlint. How to do this depends on how you call ktlint.

Below are some examples:

#### [Kotlinter](https://github.com/jeremymailen/kotlinter-gradle)

```kotlin
plugins {
    kotlin("jvm") version "<kotlin-version>"
    id("org.jmailen.kotlinter") version "<kotlinter-version>"
}

dependencies {
    ktlint("com.f43nd1r.ktlint:room-ktlint-plugin:<version>")
}
```

#### [ktlint-gradle](https://github.com/JLLeitschuh/ktlint-gradle)

```kotlin
plugins {
    kotlin("jvm") version "<kotlin-version>"
    id("org.jlleitschuh.gradle.ktlint") version "<ktlint-gradle-version>"
}

dependencies {
    ktlintRuleset("com.f43nd1r.ktlint:room-ktlint-plugin:<version>")
}
```

#### [Ktlint CLI](https://pinterest.github.io/ktlint/latest/quick-start/)
```sh
ktlint -R room-ktlint-plugin.jar --relative test.kt
```


