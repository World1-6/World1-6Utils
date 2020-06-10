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

    public int getStartLine() {
        return startLine;
    }

    public void setStartLine(int startLine) {
        this.startLine = startLine;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getBackPage() {
        return backPage;
    }

    public void setBackPage(String backPage) {
        this.backPage = backPage;
    }
}
