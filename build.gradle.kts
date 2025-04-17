plugins {
    `maven-publish`
    id("fabric-loom") version "1.10.5"
    kotlin("jvm") version "2.1.10"
    kotlin("plugin.serialization") version "2.0.0"
    id("me.modmuss50.mod-publish-plugin") version "0.8.4"
}

base {
    archivesName = "${project.property("archives_base_name")}-${project.property("mod_version")}"
}

loom {
    accessWidenerPath = file("src/main/resources/craftmine-fixes.accesswidener")
}

repositories {
    fun strictMaven(url: String, alias: String, vararg groups: String) = exclusiveContent {
        forRepository { maven(url) { name = alias } }
        filter { groups.forEach(::includeGroup) }
    }
    mavenCentral()
    strictMaven("https://www.cursemaven.com", "CurseForge", "curse.maven")
    strictMaven("https://api.modrinth.com/maven", "Modrinth", "maven.modrinth")
    maven("https://maven.creeperhost.net")
}

dependencies {
    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_version")}")
}

java {
    withSourcesJar()
    targetCompatibility = JavaVersion.toVersion(21)
    sourceCompatibility = JavaVersion.toVersion(21)
}

kotlin {
    jvmToolchain(21)
}

tasks.processResources {
    val map = mapOf(
        "version" to project.property("mod_version"),
    )

    filesMatching("fabric.mod.json") { expand(map) }
}

publishMods {
    file = tasks.remapJar.get().archiveFile
    version = project.property("mod_version") as String
    displayName = "Craftmine-Bug-Fixes ${version.get()}"
    changelog = rootProject.file("CHANGELOG.md").readText()
    type = STABLE
    modLoaders.add("fabric")

//    dryRun = providers.environmentVariable("MODRINTH_TOKEN")
//        .getOrNull() == null || providers.environmentVariable("CURSEFORGE_TOKEN").getOrNull() == null

    modrinth {
        projectId = property("publish.modrinth").toString()
        accessToken = providers.environmentVariable("MODRINTH_TOKEN")
        minecraftVersions.add("25w14craftmine")
    }

    curseforge {
        projectId = property("publish.curseforge").toString()
        accessToken = providers.environmentVariable("CURSEFORGE_TOKEN")
        minecraftVersions.add("1.21.5")
    }
}
