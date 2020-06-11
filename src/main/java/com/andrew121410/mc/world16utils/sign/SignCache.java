package com.andrew121410.mc.world16utils.sign;

import org.bukkit.block.Sign;

import java.util.NoSuchElementException;
import java.util.Objects;

public class SignCache {

    private String line0 = null;
    private String line1 = null;
    private String line2 = null;
    private String line3 = null;

    public SignCache() {
    }

    public SignCache(Sign sign) {
        this.line0 = sign.getLine(0);
        this.line1 = sign.getLine(1);
        this.line2 = sign.getLine(2);
        this.line3 = sign.getLine(3);
    }

    public void fromSign(Sign sign) {
        this.line0 = sign.getLine(0);
        this.line1 = sign.getLine(1);
        this.line2 = sign.getLine(2);
        this.line3 = sign.getLine(3);
    }

    public boolean update(Sign sign) {
        sign.setLine(0, this.line0);
        sign.setLine(1, this.line1);
        sign.setLine(2, this.line2);
        sign.setLine(3, this.line3);
        return sign.update();
    }

    public void setLine(int lineNumber, String lineString) {
        switch (lineNumber) {
            case 0:
                this.line0 = lineString;
                break;
            case 1:
                this.line1 = lineString;
                break;
            case 2:
                this.line2 = lineString;
                break;
            case 3:
                this.line3 = lineString;
                break;
            default:
                throw new NoSuchElementException("Not found.");
        }
    }

    public String getLine0() {
        return line0;
    }

    public void setLine0(String line0) {
        this.line0 = line0;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getLine3() {
        return line3;
    }

    public void setLine3(String line3) {
        this.line3 = line3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SignCache signCache = (SignCache) o;
        return Objects.equals(line0, signCache.line0) &&
                Objects.equals(line1, signCache.line1) &&
                Objects.equals(line2, signCache.line2) &&
                Objects.equals(line3, signCache.line3);
    }

    @Override
    public int hashCode() {
        return Objects.hash(line0, line1, line2, line3);
    }

    @Override
    public String toString() {
        return "SignCache{" +
                "line0='" + line0 + '\'' +
                ", line1='" + line1 + '\'' +
                ", line2='" + line2 + '\'' +
                ", line3='" + line3 + '\'' +
                '}';
    }
}