plugins {
    id("java")
    id("fabric-loom") version("1.11-SNAPSHOT")
}

class ModData {
    var modName = project.findProperty("mod.name") as String
    var modID = project.findProperty("mod.id") as String
    var modDescription = project.findProperty("mod.description") as String
    var modVersion = project.findProperty("mod.version") as String
    var modGroup = project.findProperty("mod.group") as String
    val modLicense = project.findProperty("mod.license") as String
}

class FabricData {
    var minecraftVersion = project.findProperty("fabric.minecraftVersion") as String
    var mappingsVersion = project.findProperty("fabric.mappingsVersion") as String
    var loaderVersion = project.findProperty("fabric.loaderVersion") as String
}

class DependencyData {
    var fabricAPIVersion = project.findProperty("dependencies.fabricAPIVersion") as String
    var mixinConstraintsVersion = project.findProperty("dependencies.mixinConstraintsVersion")
}

val modData = ModData()
val fabricData = FabricData()
val dependencyData = DependencyData()

group = modData.modGroup
version = modData.modVersion

base {
    archivesName.set(modData.modID)
}

dependencies {
    minecraft("com.mojang:minecraft:${fabricData.minecraftVersion}")
    mappings("net.fabricmc:yarn:${fabricData.mappingsVersion}:v2")

    modImplementation("net.fabricmc:fabric-loader:${fabricData.loaderVersion}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${dependencyData.fabricAPIVersion}")
    modImplementation("com.moulberry:mixinconstraints:${dependencyData.mixinConstraintsVersion}")
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.jar {
    inputs.property("archivesName", project.base.archivesName)

    from("LICENSE") {
        rename { "${it}_${inputs.properties["archivesName"]}" }
    }
}

tasks.processResources {
    inputs.property("name", modData.modName)
    inputs.property("id", modData.modID)
    inputs.property("description", modData.modDescription)
    inputs.property("version", modData.modVersion)
    inputs.property("license", modData.modLicense)
    inputs.property("loader", fabricData.loaderVersion)
    inputs.property("minecraft", fabricData.minecraftVersion)

    filesMatching("fabric.mod.json") {
        expand(
            "name" to modData.modName,
            "id" to modData.modID,
            "description" to modData.modDescription,
            "version" to modData.modVersion,
            "license" to modData.modLicense,
            "loader" to fabricData.loaderVersion,
            "minecraft" to fabricData.minecraftVersion
        )
    }
}