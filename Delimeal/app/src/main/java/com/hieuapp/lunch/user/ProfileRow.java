package com.hieuapp.lunch.user;

/**
 * Created by hieuapp on 24/03/2017.
 */

public class ProfileRow {
    private String label;
    private String  value;
    private int icon;

    public ProfileRow(String label, String value, int icon){
        this.label = label;
        this.value = value;
        this.icon = icon;
    }

    public String getLabel(){
        return this.label;
    }

    public String getValue(){
        return this.value;
    }

    public int getIcon(){
        return this.icon;
    }
}
