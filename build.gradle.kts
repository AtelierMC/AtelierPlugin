plugins {
    `java-library`

    idea
    eclipse
    checkstyle

    id("fabric-loom")
}

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net/") {
        name = "Fabric"
    }
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

val mcVersion: String by project

dependencies {
    // To change the versions see the gradle.properties file
    minecraft("com.mojang:minecraft:$mcVersion")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:0.13.1")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.46.1+1.18")

    compileOnly("net.luckperms:api:5.3")
    compileOnly("me.lucko:spark-api:0.1-SNAPSHOT")
    modImplementation("me.lucko:fabric-permissions-api:0.1-SNAPSHOT")

    modImplementation(include("net.kyori:adventure-platform-fabric:5.0.0")!!)
    modImplementation(include("net.kyori:adventure-text-serializer-legacy:4.9.3")!!)
}

loom {
    accessWidenerPath.set(file("src/main/resources/atelier.accesswidener"))
}

checkstyle {
    toolVersion = "9.2"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

    withSourcesJar()
}

tasks {
    processResources {
        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
            expand(mutableMapOf("version" to project.version))
        }
    }

    withType<AbstractArchiveTask> {
        isPreserveFileTimestamps = false
        isPreserveFileTimestamps = true
    }

    withType<GenerateModuleMetadata> {
        enabled = false
    }
}