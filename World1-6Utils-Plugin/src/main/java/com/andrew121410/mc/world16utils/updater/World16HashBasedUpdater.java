package com.andrew121410.mc.world16utils.updater;

import com.andrew121410.ccutils.utils.HashBasedUpdater;

import java.io.File;

public class World16HashBasedUpdater extends HashBasedUpdater {
    public World16HashBasedUpdater(Class<?> aClass, String urlOfJar, String urlOfHash) {
        super(convert(new File(aClass.getProtectionDomain().getCodeSource().getLocation().getFile())), urlOfJar, urlOfHash);
    }

    public World16HashBasedUpdater(File currentFileLocation, String urlOfJar, String urlOfHash) {
        super(convert(currentFileLocation), urlOfJar, urlOfHash);
    }

    public World16HashBasedUpdater(File currentFileLocation, String urlOfJar, String urlOfHash, File optionalNewLocation) {
        super(convert(currentFileLocation), urlOfJar, urlOfHash, optionalNewLocation);
    }

    /*
     * On Paper 1.20.6 and above plugins get remapped before starting the server, and
     * The plugin will get loaded in plugins/.paper-remapped/ instead of plugins/
     * So we need to convert the path to the correct path. If needed
     */
    private static File convert(File file) {
        String path = file.getPath();
        if (path.contains(".paper-remapped")) {
            // Handle cases with or without trailing slashes
            path = path.replace(".paper-remapped/", "").replace(".paper-remapped", "");
        }
        return new File(path);
    }
}
