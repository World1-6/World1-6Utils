package com.andrew121410.mc.world16utils.utils;

import io.papermc.paper.ServerBuildInfo;
import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MinecraftVersions {
    /**
     * Matches release Minecraft versions in both schemes:
     * - Legacy style: 1.21.11 (major.minor.patch)
     * - New style:    26.1    (major.minor, patch optional)
     * <p>
     * Patch defaults to 0 when omitted (e.g. 26.1 -> 26.1.0 for comparison).
     * Pre-release/snapshot ids (e.g. 26.1-pre1, 23w31a) are treated as non-release.
     */
    private static final Pattern RELEASE_VERSION_PATTERN = Pattern.compile("^(\\d+)\\.(\\d+)(?:\\.(\\d+))?$");

    private MinecraftVersions() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Machine-friendly version id.
     * Examples: 1.20.6, 1.20.2-pre2, 23w31a
     */
    public static String minecraftVersionId() {
        try {
            String id = ServerBuildInfo.buildInfo().minecraftVersionId();
            if (id != null && !id.isBlank()) {
                return id;
            }
        } catch (Throwable ignored) {
            // Fallback below
        }

        try {
            String v = Bukkit.getMinecraftVersion();
            if (v != null && !v.isBlank()) {
                return v;
            }
        } catch (Throwable ignored) {
            // Fallback below
        }

        // Typical Bukkit value: "1.21.11-R0.1-SNAPSHOT"
        String bukkitVersion = Bukkit.getBukkitVersion();
        int dashIndex = bukkitVersion.indexOf('-');
        return dashIndex >= 0 ? bukkitVersion.substring(0, dashIndex) : bukkitVersion;
    }

    /**
     * Human-friendly version name.
     */
    public static String minecraftVersionName() {
        try {
            String name = ServerBuildInfo.buildInfo().minecraftVersionName();
            if (name != null && !name.isBlank()) {
                return name;
            }
        } catch (Throwable ignored) {
            // Fallback below
        }

        return minecraftVersionId();
    }

    public static boolean isReleaseVersion() {
        return parseReleaseVersion(minecraftVersionId()) != null;
    }

    public static boolean isAtLeast(int major, int minor, int patch) {
        int[] current = parseReleaseVersion(minecraftVersionId());
        if (current == null) {
            return false;
        }
        return compare(current, new int[]{major, minor, patch}) >= 0;
    }

    public static boolean isAtLeast(String version) {
        int[] target = parseReleaseVersion(version);
        if (target == null) {
            throw new IllegalArgumentException("Invalid release version: " + version);
        }

        int[] current = parseReleaseVersion(minecraftVersionId());
        if (current == null) {
            return false;
        }

        return compare(current, target) >= 0;
    }

    private static int[] parseReleaseVersion(String version) {
        if (version == null) {
            return null;
        }

        Matcher matcher = RELEASE_VERSION_PATTERN.matcher(version.trim());
        if (!matcher.matches()) {
            return null;
        }

        int major = Integer.parseInt(matcher.group(1));
        int minor = Integer.parseInt(matcher.group(2));
        int patch = matcher.group(3) == null ? 0 : Integer.parseInt(matcher.group(3));
        return new int[]{major, minor, patch};
    }

    private static int compare(int[] a, int[] b) {
        for (int i = 0; i < 3; i++) {
            if (a[i] != b[i]) {
                return Integer.compare(a[i], b[i]);
            }
        }
        return 0;
    }
}
