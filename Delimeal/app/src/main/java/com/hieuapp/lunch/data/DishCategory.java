package com.hieuapp.lunch.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hieuapp on 09/03/2017.
 */

public class DishCategory {
    public static final int FAMILY_FRIENDS = 1;
    public static final int COFFEE = 2;
    public static final int BAKERY = 3;
    public static final int FAST_FOOD = 4;
    public static final int SNACKS = 5;
    public static final int LUNCH = 5;

    @SerializedName("category")
    private String categoryName;
    @SerializedName("total")
    private int numberDishes;
    @SerializedName("dishes")
    private List<Dish> dishes;


    public String getCategoryName() {
        return categoryName;
    }

    public int getNumberDishes() {
        return numberDishes;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setNumberDishes(int numberDishes) {
        this.numberDishes = numberDishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }
}
