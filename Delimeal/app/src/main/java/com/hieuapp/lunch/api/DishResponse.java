package com.hieuapp.lunch.api;

import com.google.gson.annotations.SerializedName;
import com.hieuapp.lunch.data.Dish;

import java.util.List;

/**
 * Created by hieuapp on 19/04/2017.
 */

public class DishResponse {
    @SerializedName("dishes")
    private List<Dish> dishes;

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }
}
