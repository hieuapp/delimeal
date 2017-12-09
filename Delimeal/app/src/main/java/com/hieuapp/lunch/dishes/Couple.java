package com.hieuapp.lunch.dishes;

/**
 * Created by hieuapp on 04/05/2017.
 */

public class Couple {

    private String attribute;
    private String value1;
    private String value2;
    private int icon;

    public Couple(int icon, String name, String value1, String value2){
        this.icon = icon;
        this.attribute = name;
        this.value1 = value1;
        this.value2 = value2;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
