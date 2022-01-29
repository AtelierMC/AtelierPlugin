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
}

val mcVersion: String by project

dependencies {
    // To change the versions see the gradle.properties file
    minecraft("com.mojang:minecraft:$mcVersion")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:0.12.12")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.46.1+1.18")

    compileOnly("net.luckperms:api:5.3")
}

loom {
    //ccessWidenerPath.set(file("src/main/resources/sekiei.accesswidener"))
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