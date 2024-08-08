plugins {
    id("com.gradleup.shadow") version "8.3.0" // https://github.com/GradleUp/shadow
    `maven-publish`
}

description = "World1-6Utils-Plugin"

dependencies {
    api(project(":World1-6Utils-COMMON"))
    api(project(":World1-6Utils-API"))
    api(project(":World1-6Utils_WE_7210"))

    // Other
    api("com.github.andrew121410:CCUtilsJava:ba20482ccb")
    compileOnly("org.geysermc.floodgate:api:2.2.3-SNAPSHOT") // https://github.com/GeyserMC/Floodgate/blob/master/gradle.properties
    api("org.spongepowered:configurate-yaml:4.2.0-SNAPSHOT")
}

tasks {
    build {
        dependsOn("shadowJar")
    }

    jar {
        enabled = false
    }

    shadowJar {
        // Yes all three of these are needed for JitPack to work.
        archiveBaseName.set("World1-6Utils")
        archiveClassifier.set("")
        archiveVersion.set("")

        relocate("com.andrew121410.ccutils.", "com.andrew121410.mc.world16utils.utils.ccutils.")
        relocate("org.spongepowered.", "com.andrew121410.mc.world16utils.utils.spongepowered.")
        exclude("META-INF/*.MF", "META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifact(tasks["shadowJar"])
        }
    }
}