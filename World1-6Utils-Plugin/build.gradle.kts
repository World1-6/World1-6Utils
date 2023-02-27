plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
    `maven-publish`
}

description = "World1-6Utils-Plugin"

dependencies {
    api(project(":World1-6Utils-COMMON"))
    api(project(":World1-6Utils-API"))
    api(project(":World1-6Utils_WE_7210"))
    implementation("com.github.andrew121410:CCUtilsJava:0dc8d953")
    compileOnly("org.geysermc.floodgate:api:2.0-SNAPSHOT")
}

tasks {
    build {
        dependsOn("shadowJar")
    }

    jar {
        enabled = false
    }

    shadowJar {
        archiveBaseName.set("World1-6Utils")
        archiveClassifier.set("")
        archiveVersion.set("")

        relocate("com.andrew121410.ccutils.", "com.andrew121410.mc.world16utils.utils.ccutils.")
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