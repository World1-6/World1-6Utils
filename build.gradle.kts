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
            url = uri("https://repo.papermc.io/repository/maven-public/")
        }

        maven {
            url = uri("https://jitpack.io")
        }

        maven {
            url = uri("https://maven.enginehub.org/repo/")
        }
        maven {
            url = uri("https://repo.opencollab.dev/main/")
        }
        maven {
            url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
        }
    }

    java {
        // Do I need all of these? I have no idea lol.
        toolchain.languageVersion.set(JavaLanguageVersion.of(21))
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    dependencies {
        compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}