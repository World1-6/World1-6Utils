package com.andrew121410.mc.world16utils.sign;

import net.kyori.adventure.text.Component;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;

import java.util.NoSuchElementException;
import java.util.Objects;

public class SignCache {

    private final Side side;

    private Component line0 = Component.empty();
    private Component line1 = Component.empty();
    private Component line2 = Component.empty();
    private Component line3 = Component.empty();

    public SignCache() {
        this.side = Side.FRONT;
    }

    public SignCache(Side side) {
        this.side = side;
    }

    public SignCache(Sign sign, Side side) {
        this.side = side;

        SignSide signSide = sign.getSide(this.side);
        this.line0 = signSide.line(0);
        this.line1 = signSide.line(1);
        this.line2 = signSide.line(2);
        this.line3 = signSide.line(3);
    }

    public void fromSign(Sign sign) {
        SignSide signSide = sign.getSide(this.side);
        this.line0 = signSide.line(0);
        this.line1 = signSide.line(1);
        this.line2 = signSide.line(2);
        this.line3 = signSide.line(3);
    }

    public boolean update(Sign sign) {
        SignSide signSide = sign.getSide(this.side);
        signSide.line(0, this.line0);
        signSide.line(1, this.line1);
        signSide.line(2, this.line2);
        signSide.line(3, this.line3);
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
        if (o == null || getClass() != o.getClass()) return false;
        SignCache signCache = (SignCache) o;
        return side == signCache.side && Objects.equals(line0, signCache.line0) && Objects.equals(line1, signCache.line1) && Objects.equals(line2, signCache.line2) && Objects.equals(line3, signCache.line3);
    }

    @Override
    public int hashCode() {
        return Objects.hash(side, line0, line1, line2, line3);
    }

    @Override
    public String toString() {
        return "SignCache{" +
                "side=" + side +
                ", line0=" + line0 +
                ", line1=" + line1 +
                ", line2=" + line2 +
                ", line3=" + line3 +
                '}';
    }
}