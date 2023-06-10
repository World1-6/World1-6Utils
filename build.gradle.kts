plugins {
    `java-library`
}

allprojects {
    group = "com.andrew121410.mc"
    version = "1.0"
}

subprojects {
    apply(plugin = "java-library")

    repositories {
        mavenCentral()

        maven {
            url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        }

        maven {
            url = uri("https://repo.papermc.io/repository/maven-public/")
        }

        maven {
            url = uri("https://jitpack.io")
        }

        maven {
            url = uri("https://maven.enginehub.org/repo/")
        }
        maven {
            url = uri("https://repo.opencollab.dev/maven-snapshots/")
        }
    }

    dependencies {
        compileOnly("io.papermc.paper:paper-api:1.20-R0.1-SNAPSHOT")
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}