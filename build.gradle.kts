import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    `java-library`
    id("io.papermc.paperweight.userdev") version "1.3.1"
    id("xyz.jpenilla.run-paper") version "1.0.6"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

group = "ga.caomile"
version = "1.0.0-SNAPSHOT"
description = "AtelierMC core plugin"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    paperDevBundle("1.18-R0.1-SNAPSHOT")

    implementation("net.kyori:adventure-text-minimessage:4.1.0-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.10.10")
}

tasks {
    val copyToTestServer = register<org.gradle.api.tasks.Copy>("copyToTestServer") {
        from("build/libs/atelier-1.0.0-SNAPSHOT-dev-all.jar")
        into("C:/Users/JustMango/Desktop/Test/plugins")
    }


    build {
        dependsOn(reobfJar)
        dependsOn(shadowJar)
        finalizedBy(copyToTestServer)
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }
}

// Configure plugin.yml generation
bukkit {
    load = BukkitPluginDescription.PluginLoadOrder.STARTUP
    main = "ga.caomile.atelier.Atelier"
    apiVersion = "1.18"
    authors = listOf("JustMangoT")
}