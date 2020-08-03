package com.andrew121410.mc.world16utils.blocks.sign.screen.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SignLayout {

    private String name;
    private String reverseLayout;

    private int numberOfPages = 0;

    private List<SignPage> signPages;

    public SignLayout(String name, String reverseLayout) {
        this.name = name;
        this.reverseLayout = reverseLayout;
        this.signPages = new ArrayList<>();
    }

    public void addSignPage(SignPage signPage) {
        this.signPages.add(new SignPage(signPage, this.numberOfPages));
        this.numberOfPages++;
    }

    public SignPage getSignPage(int number) {
        try {
            return this.signPages.get(number);
        } catch (Exception exception) {
            return null;
        }
    }

    public SignPage getReversePage(int currentPage) {
        int newInt = currentPage - 1;
        if (newInt == -1) return null;
        return this.signPages.get(currentPage - 1);
    }

    public SignPage getNextPage(int currentPage) {
        int newInt = currentPage + 1;
        if (newInt >= numberOfPages) return null;
        return this.signPages.get(currentPage + 1);
    }

    public String getName() {
        return name;
    }

    public String getReverseLayout() {
        return reverseLayout;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SignLayout that = (SignLayout) o;
        return numberOfPages == that.numberOfPages &&
                Objects.equals(name, that.name) &&
                Objects.equals(reverseLayout, that.reverseLayout) &&
                Objects.equals(signPages, that.signPages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, reverseLayout, numberOfPages, signPages);
    }

    @Override
    public String toString() {
        return "SignLayout{" +
                "name='" + name + '\'' +
                ", reverseLayout='" + reverseLayout + '\'' +
                ", numberOfPages=" + numberOfPages +
                ", signPages=" + signPages +
                '}';
    }
}
