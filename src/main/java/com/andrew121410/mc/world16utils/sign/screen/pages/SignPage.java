package com.andrew121410.mc.world16utils.sign.screen.pages;

import com.andrew121410.mc.world16utils.sign.SignCache;

import java.util.*;

public class SignPage {

    private Map<Integer, SignLinePattern> signLinePatternMap;
    private String[] topRawPattern;

    private String name;
    private String backPage;
    private int pageNumber;

    private int startLine;
    private int min;
    private int max;

    private String line0 = "";
    private String line1 = "";
    private String line2 = "";
    private String line3 = "";

    public SignPage(String name, String backPage, int startLine, int min, int max, String[] pattern) {
        this.name = name;
        this.backPage = backPage;
        this.startLine = startLine;
        this.min = min;
        this.max = max;
        this.topRawPattern = pattern;

        if (this.topRawPattern == null) {
            this.topRawPattern = new String[]{"*", "*", "*", "*"};
        }

        this.signLinePatternMap = new HashMap<>();
    }

    protected SignPage(SignPage signPage, int pageNumber) {
        this.name = signPage.name;
        this.backPage = signPage.backPage;
        this.startLine = signPage.startLine;
        this.min = signPage.min;
        this.max = signPage.max;

        this.line0 = signPage.line0;
        this.line1 = signPage.line1;
        this.line2 = signPage.line2;
        this.line3 = signPage.line3;

        this.pageNumber = pageNumber;

        this.topRawPattern = signPage.topRawPattern;
        this.signLinePatternMap = signPage.signLinePatternMap;

        int index = 0;
        for (String linePattern : this.topRawPattern) {
            SignLinePattern signLinePattern = new SignLinePattern(index, linePattern);
            this.signLinePatternMap.put(index, signLinePattern);
            index++;
        }
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

    public Map<Integer, SignLinePattern> getSignLinePatternMap() {
        return signLinePatternMap;
    }

    public String[] getTopRawPattern() {
        return topRawPattern;
    }

    public String getName() {
        return name;
    }

    public String getBackPage() {
        return backPage;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getStartLine() {
        return startLine;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public String getLine0() {
        return line0;
    }

    public String getLine1() {
        return line1;
    }

    public String getLine2() {
        return line2;
    }

    public String getLine3() {
        return line3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SignPage signPage = (SignPage) o;
        return pageNumber == signPage.pageNumber &&
                startLine == signPage.startLine &&
                min == signPage.min &&
                max == signPage.max &&
                Objects.equals(signLinePatternMap, signPage.signLinePatternMap) &&
                Arrays.equals(topRawPattern, signPage.topRawPattern) &&
                Objects.equals(name, signPage.name) &&
                Objects.equals(backPage, signPage.backPage) &&
                Objects.equals(line0, signPage.line0) &&
                Objects.equals(line1, signPage.line1) &&
                Objects.equals(line2, signPage.line2) &&
                Objects.equals(line3, signPage.line3);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(signLinePatternMap, name, backPage, pageNumber, startLine, min, max, line0, line1, line2, line3);
        result = 31 * result + Arrays.hashCode(topRawPattern);
        return result;
    }

    @Override
    public String toString() {
        return "SignPage{" +
                "signLinePatternMap=" + signLinePatternMap +
                ", topRawPattern=" + Arrays.toString(topRawPattern) +
                ", name='" + name + '\'' +
                ", backPage='" + backPage + '\'' +
                ", pageNumber=" + pageNumber +
                ", startLine=" + startLine +
                ", min=" + min +
                ", max=" + max +
                ", line0='" + line0 + '\'' +
                ", line1='" + line1 + '\'' +
                ", line2='" + line2 + '\'' +
                ", line3='" + line3 + '\'' +
                '}';
    }
}