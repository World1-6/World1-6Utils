@file:Suppress("UnstableApiUsage")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
}

rootProject.name = "world16utils-parent"

include(":World1-6Utils_CMI_API")
include(":World1-6Utils-COMMON")
include(":World1-6Utils-API")
include(":World1-6Utils_WE_7210")
include(":World1-6Utils-Plugin")