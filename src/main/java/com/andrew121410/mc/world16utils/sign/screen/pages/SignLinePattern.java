package com.andrew121410.mc.world16utils.sign.screen.pages;

import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignLinePattern {

    private int lineNumber;
    private String pattern;

    private int minSide = 1;
    private int maxSide = 1;

    private Map<Integer, Integer> sideIndexMap;

    public SignLinePattern(int lineNumber, String pattern) {
        this.lineNumber = lineNumber;
        this.sideIndexMap = new HashMap<>();
        this.pattern = pattern.replaceAll("#", " ");
        doPreCacMap();
    }

    private void doPreCacMap() {
        int howManySides = 0;
        char[] chars = pattern.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            String stringChar = String.valueOf(chars[i]);

            if (stringChar.equalsIgnoreCase("*")) {
                howManySides++;
                this.sideIndexMap.put(howManySides, i);
            }
        }

        this.sideIndexMap.forEach((k, v) -> Bukkit.broadcastMessage("sideIndexMap -> KEY: " + k + " VALUE" + v));

        this.maxSide = howManySides;
    }

    public Integer getIndexOfSide(int side) {
        return this.sideIndexMap.get(side);
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getPattern() {
        return pattern;
    }

    public int getMinSide() {
        return minSide;
    }

    public int getMaxSide() {
        return maxSide;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SignLinePattern that = (SignLinePattern) o;
        return lineNumber == that.lineNumber &&
                minSide == that.minSide &&
                maxSide == that.maxSide &&
                Objects.equals(pattern, that.pattern) &&
                Objects.equals(sideIndexMap, that.sideIndexMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineNumber, pattern, minSide, maxSide, sideIndexMap);
    }

    @Override
    public String toString() {
        return "SignLinePattern{" +
                "lineNumber=" + lineNumber +
                ", pattern='" + pattern + '\'' +
                ", minSide=" + minSide +
                ", maxSide=" + maxSide +
                ", sideIndexMap=" + sideIndexMap +
                '}';
    }
}
