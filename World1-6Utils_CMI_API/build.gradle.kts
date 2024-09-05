plugins {
    id("world16utils.java-conventions")
    `maven-publish`
}

description = "World1-6Utils_CMI_API"

dependencies {
    compileOnly(project(":World1-6Utils-COMMON"))
    compileOnly(project(":World1-6Utils-API"))
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}