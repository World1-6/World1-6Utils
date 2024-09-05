plugins {
    id("world16utils.java-conventions")
}

dependencies {
    compileOnly(project(":World1-6Utils-COMMON"))
    compileOnly(project(":World1-6Utils-API"))

    compileOnly(libs.worldedit.core)
    compileOnly(libs.worldedit.bukkit)
}

description = "World1-6Utils_WE_7210"