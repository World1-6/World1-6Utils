description = "World1-6Utils-API"

plugins {
    id("world16utils.java-conventions")
    `maven-publish`
}

dependencies {
    compileOnly(project(":World1-6Utils-COMMON"))
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}