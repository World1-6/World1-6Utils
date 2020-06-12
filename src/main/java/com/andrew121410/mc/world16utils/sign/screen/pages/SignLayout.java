package com.andrew121410.mc.world16utils.sign.screen.pages;

import java.util.ArrayList;
import java.util.List;

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
        return this.signPages.get(number);
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
}
