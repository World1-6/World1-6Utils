package com.andrew121410.mc.world16utils.sign.screen.pages;

import com.andrew121410.mc.world16utils.sign.SignCache;

import java.util.NoSuchElementException;

public class SignPage {

    private String name;
    private String backPage;
    private int pageNumber;

    private int startLine;
    private int min;
    private int max;

    private String line0;
    private String line1;
    private String line2;
    private String line3;

    public SignPage(String name, String backPage, int startLine, int min, int max) {
        this.name = name;
        this.backPage = backPage;
        this.startLine = startLine;
        this.min = min;
        this.max = max;
    }

    protected SignPage(SignPage signPage, int pageNumber) {
        this.name = signPage.getName();
        this.backPage = signPage.getBackPage();
        this.startLine = signPage.startLine;
        this.min = signPage.getMin();
        this.max = signPage.getMax();

        this.pageNumber = pageNumber;
    }

    public String getLine(int lineNumber) {
        switch (lineNumber) {
            case 0:
                return this.line0;
            case 1:
                return this.line1;
            case 2:
                return this.line2;
            case 3:
                return this.line3;
            default:
                return null;
        }
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

    public SignCache toSignCache() {
        SignCache signCache = new SignCache();
        signCache.setLine(0, line0);
        signCache.setLine(1, line1);
        signCache.setLine(2, line2);
        signCache.setLine(3, line3);
        return signCache;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBackPage() {
        return backPage;
    }

    public void setBackPage(String backPage) {
        this.backPage = backPage;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
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
}