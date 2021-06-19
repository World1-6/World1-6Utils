package com.andrew121410.mc.world16utils.blocks;

public enum MarkerColor {
    BLACK(-2130771968),
    GREEN(-2147418368);

    private int color;

    MarkerColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}
