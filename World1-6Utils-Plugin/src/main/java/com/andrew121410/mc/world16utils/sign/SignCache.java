package com.andrew121410.mc.world16utils.sign;

import net.kyori.adventure.text.Component;
import org.bukkit.block.Sign;

import java.util.NoSuchElementException;
import java.util.Objects;

public class SignCache {

    private Component line0 = Component.empty();
    private Component line1 = Component.empty();
    private Component line2 = Component.empty();
    private Component line3 = Component.empty();

    public SignCache() {
    }

    public SignCache(Sign sign) {
        this.line0 = sign.line(0);
        this.line1 = sign.line(1);
        this.line2 = sign.line(2);
        this.line3 = sign.line(3);
    }

    public void fromSign(Sign sign) {
        this.line0 = sign.line(0);
        this.line1 = sign.line(1);
        this.line2 = sign.line(2);
        this.line3 = sign.line(3);
    }

    public boolean update(Sign sign) {
        sign.line(0, this.line0);
        sign.line(1, this.line1);
        sign.line(2, this.line2);
        sign.line(3, this.line3);
        return sign.update();
    }

    public Component getLine(int lineNumber) {
        return switch (lineNumber) {
            case 0 -> this.line0;
            case 1 -> this.line1;
            case 2 -> this.line2;
            case 3 -> this.line3;
            default -> null;
        };
    }

    public void setLine(int lineNumber, Component component) {
        switch (lineNumber) {
            case 0 -> this.line0 = component;
            case 1 -> this.line1 = component;
            case 2 -> this.line2 = component;
            case 3 -> this.line3 = component;
            default -> throw new NoSuchElementException("Not found.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SignCache signCache)) return false;

        if (!Objects.equals(line0, signCache.line0)) return false;
        if (!Objects.equals(line1, signCache.line1)) return false;
        if (!Objects.equals(line2, signCache.line2)) return false;
        return Objects.equals(line3, signCache.line3);
    }

    @Override
    public int hashCode() {
        int result = line0 != null ? line0.hashCode() : 0;
        result = 31 * result + (line1 != null ? line1.hashCode() : 0);
        result = 31 * result + (line2 != null ? line2.hashCode() : 0);
        result = 31 * result + (line3 != null ? line3.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SignCache{" +
                "line0=" + line0 +
                ", line1=" + line1 +
                ", line2=" + line2 +
                ", line3=" + line3 +
                '}';
    }
}