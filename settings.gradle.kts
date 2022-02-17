pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/") {
            name = "Fabric"
        }
        mavenCentral()
        gradlePluginPortal()
    }

    plugins {
        id("fabric-loom") version "0.11-SNAPSHOT"
    }
}

rootProject.name = "atelier"
