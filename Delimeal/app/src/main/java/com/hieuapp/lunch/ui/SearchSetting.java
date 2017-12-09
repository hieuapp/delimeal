package com.hieuapp.lunch.ui;

/**
 * Created by hieuapp on 12/05/2017.
 */

public class SearchSetting {

    private String label;

    private int id;

    public SearchSetting(String label, int id){
        this.label = label;
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
