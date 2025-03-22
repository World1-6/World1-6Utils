[![Release](https://jitpack.io/v/World1-6/World1-6Utils.svg)](https://jitpack.io/#World1-6/World1-6Utils)

# World1-6Utils
A library shared between all my plugins.

As of 7/26/2022, this library now requires Paper, instead of Spigot.

## Supported Versions:
- 1.21.4

## Modules:
1. World1-6Utils-COMMON: *Classes that are shared between modules*
2. World1-6Utils-API: *Interfaces used for abstraction*
3. World1-6Utils-Plugin: *Classes that can be used in any MC versions via regular Bukkit API or our ClassWrappers API*
4. World1-6Utils_WE_7210: *Used to access the 7.2.10 WorldEdit API used for a class that implements World1-6Utils-API*
5. World1-6Utils_CMI_API: *Used to access CMI API (Yes I'm aware of https://github.com/Zrips/CMI-API)*

## Compiling:
`./gradlew build`