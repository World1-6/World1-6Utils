package com.andrew121410.mc.world16utils.sign.screen.pages;

import java.util.HashMap;
import java.util.Map;

public class SignOS {

    private String name;

    private Map<String, SignLayout> signLayoutMap;

    public SignOS(String name) {
        this.name = name;
        this.signLayoutMap = new HashMap<>();
    }

    public void putSignLayout(SignLayout signLayout) {
        this.signLayoutMap.putIfAbsent(signLayout.getName(), signLayout);
    }

    public SignLayout getSignLayout(String name) {
        return this.signLayoutMap.get(name);
    }

    public String getName() {
        return name;
    }
}