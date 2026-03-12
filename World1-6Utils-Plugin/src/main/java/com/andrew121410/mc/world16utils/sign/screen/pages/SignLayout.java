package com.andrew121410.mc.world16utils.sign.screen.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SignLayout {

    private String name;
    private String parentLayout;

    private int numberOfPages = 0;

    private List<SignPage> signPages;

    public SignLayout(String name, String parentLayout) {
        this.name = name;
        this.parentLayout = parentLayout;
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

    public SignPage getPreviousPage(int currentPage) {
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

    public String getParentLayout() {
        return parentLayout;
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
                Objects.equals(parentLayout, that.parentLayout) &&
                Objects.equals(signPages, that.signPages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, parentLayout, numberOfPages, signPages);
    }

    @Override
    public String toString() {
        return "SignLayout{" +
                "name='" + name + '\'' +
                ", parentLayout='" + parentLayout + '\'' +
                ", numberOfPages=" + numberOfPages +
                ", signPages=" + signPages +
                '}';
    }
}
