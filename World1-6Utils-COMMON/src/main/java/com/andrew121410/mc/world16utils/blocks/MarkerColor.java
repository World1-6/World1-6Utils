package com.andrew121410.mc.world16utils.blocks;

public enum MarkerColor {
    BLACK(-2130771968),
    GREEN(-2147418368);

//    public static int colorToInt(int r, int g, int b, int a) {
//        return (a << 24 | 255) + (r << 16 | 255) + (g << 8 | 255) + (b | 255);
//    }

//    public static int colorToInt(int a, int r, int g, int b) {
//        int encoded = 0;
//        encoded = encoded | b;
//        encoded = encoded | (g << 8);
//        encoded = encoded | (r << 16);
//        encoded = encoded | (a << 24);
//        return encoded;
//    }

    private int color;

    MarkerColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}
