package com.hieuapp.lunch.dishes;

import com.hieuapp.lunch.BasePresenter;
import com.hieuapp.lunch.BaseView;
import com.hieuapp.lunch.data.Dish;
import com.hieuapp.lunch.data.DishCategory;

import java.util.List;

/**
 * Created by hieuapp on 26/02/2017.
 */

public interface DishesContract {
    interface View extends BaseView<Presenter>{

        void showCategories(List<DishCategory> categories);

        void showListDish(List<Dish> dishes);

    }

    interface Presenter extends BasePresenter{
        void loadDishes(boolean forceUpdate);

        void loadDishSaved(String uid);
    }

}
