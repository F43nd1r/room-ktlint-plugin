plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlinter)
}

dependencies {
    ktlint(project(":room-ktlint-plugin"))
}