package com.andrew121410.mc.world16utils.updater;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class AbstractUpdater {

    private final String urlOfJar;
    private final String urlOfHash;

    private JavaPlugin plugin;
    private String cacheOfHashFromRemote = null;
    private boolean updatedAlready = false;

    public AbstractUpdater(JavaPlugin plugin, String urlOfJar, String urlOfHash) {
        this.plugin = plugin;
        this.urlOfJar = urlOfJar;
        this.urlOfHash = urlOfHash;
    }

    public boolean shouldUpdate(boolean shouldCacheRemoteHash) {
        String hashOfCurrentJar = getHashOfCurrentJar();
        String hashFromRemote = getHashFromRemote();

        if (shouldCacheRemoteHash) this.cacheOfHashFromRemote = hashFromRemote;

        return !hashOfCurrentJar.equals(hashFromRemote);
    }

    public String update() {
        if (this.updatedAlready)
            return "An update is already in progress, or you have already updated, and need to restart your server.";
        this.updatedAlready = true;

        String tempDirectory = System.getProperty("java.io.tmpdir");
        File fileOfCurrentJar = new File(this.plugin.getClass().getProtectionDomain().getCodeSource().getLocation().getFile());

        File tempFile = new File(tempDirectory, fileOfCurrentJar.getName());
        try {
            // Download the file from the URL.
            URL website = new URL(this.urlOfJar);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(tempFile);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

            if (this.cacheOfHashFromRemote == null) {
                this.cacheOfHashFromRemote = getHashFromRemote();
            }

            // Verify the hash of the downloaded file before we replace the current one
            String hashOfDownloadedFile = getHashOfFile(tempFile);
            if (!hashOfDownloadedFile.equals(this.cacheOfHashFromRemote)) {
                this.updatedAlready = false;
                return "Hash of downloaded file does not match hash on github. Aborting update.";
            }

            Path from = tempFile.toPath();
            Path to = fileOfCurrentJar.toPath();
            java.nio.file.Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            this.updatedAlready = false;
            return "Failed to update " + this.plugin.getName();
        }

        return "Updated to latest version!";
    }

    public String getHashFromRemote() {
        String tempDirectory = System.getProperty("java.io.tmpdir");
        File hashFile = new File(tempDirectory, "hashh.txt"); // Yes I know this is "hashh" and not "hash"
        try {
            ReadableByteChannel readChannel = Channels.newChannel(new URL(this.urlOfHash).openStream());
            FileOutputStream fileOS = new FileOutputStream(hashFile);
            FileChannel writeChannel = fileOS.getChannel();
            writeChannel.transferFrom(readChannel, 0, Long.MAX_VALUE);

            String data = new String(java.nio.file.Files.readAllBytes(hashFile.toPath()));
            String[] args = data.split(" ");
            return args[0];
        } catch (Exception e) {
            return null;
        }
    }

    public String getHashOfCurrentJar() {
        try {
            File locationOfJar = new File(this.plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
            return getHashOfFile(locationOfJar);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public String getHashOfFile(File file) {
        try {
            HashCode hash = Files.asByteSource(file).hash(Hashing.sha256());
            return hash.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
