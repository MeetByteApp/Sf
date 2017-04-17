package com.example.android.sf;

/**
 * Created by Indravasan on 13-Apr-17.
 */

public class Beds {
    private String length, width, thickness;
    private String model, key;

    public Beds(){}

    Beds(String l, String w, String t, String m) {
        length = l;
        width = w;
        thickness = t;
        model = m;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLength() {
        return length;
    }

    public String getWidth() {
        return width;
    }

    public String getThickness() {
        return thickness;
    }

    public String getModel() {
        return model;
    }
}
