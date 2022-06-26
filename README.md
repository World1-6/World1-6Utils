[![Release](https://jitpack.io/v/World1-6/World1-6Utils.svg)]
(https://jitpack.io/#World1-6/World1-6Utils)

# World1-6Utils
A library shared between all my plugins.

## Modules:
1. World1-6Utils-COMMON: *Classes that are shared between modules*
2. World1-6Utils-API: *Interfaces used for abstraction*
3. World1-6Utils-Plugin: *Classes that can be used in any MC versions via regular Bukkit API or our ClassWrappers API*
4. World1-6Utils_V1_18_R2: *Used to access 1.18.2 NMS prohibited API used for classes that implement World1-6Utils-API*
5. World1-6Utils_V1_19_R1: *Used to access 1.19 NMS prohibited API used for classes that implement World1-6Utils-API*
6. World1-6Utils_WE_723: *Used to access the 7.2.3 WorldEdit API used for a class that implements World1-6Utils-API*
7. World1-6Utils_CMI_API: *Used to access CMI (Yes I know there is https://github.com/Zrips/CMI-API BUT it isn't a maven or gradle project...)*

## Compiling:
`maven clean package`