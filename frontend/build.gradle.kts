
// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
    dependencies {
        classpath ("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:2.0.1")
        classpath("io.github.gmazzo.test.aggregation:plugin:2.1.1")
    }
}
plugins {
    id("com.android.application") version "8.1.1" apply false
    id ("com.android.library") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
}
apply(plugin = "io.github.gmazzo.test.aggregation.coverage")