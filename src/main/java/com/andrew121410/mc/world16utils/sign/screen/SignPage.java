package com.andrew121410.mc.world16utils.sign.screen;

public class SignPage {

    private String name;
    private String backPage;

    private String line0;
    private String line1;
    private String line2;
    private String line3;

    private int startLine = 0;
    private int min = 0;
    private int max = 3;

    public SignPage(String name, String backPage, int startLine, int min, int max) {
        this.name = name;
        this.backPage = backPage;
        this.startLine = startLine;
        this.min = min;
        this.max = max;
    }
}
