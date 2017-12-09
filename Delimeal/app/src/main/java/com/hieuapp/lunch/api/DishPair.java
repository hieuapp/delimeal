package com.hieuapp.lunch.api;

import com.google.gson.annotations.SerializedName;
import com.hieuapp.lunch.data.DishDetail;

import java.util.List;

/**
 * Created by hieuapp on 05/05/2017.
 */

public class DishPair {
    @SerializedName("pair")
    private List<DishDetail> pair;

    public List<DishDetail> getPair() {
        return pair;
    }

    public void setPair(List<DishDetail> pair) {
        this.pair = pair;
    }
}
