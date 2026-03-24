@file:Suppress("UnstableApiUsage")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
}

rootProject.name = "world16utils-parent"

include(":World1-6Utils-COMMON")
include(":World1-6Utils-API")
include(":World1-6Utils-WorldEdit")
include(":World1-6Utils-Plugin")