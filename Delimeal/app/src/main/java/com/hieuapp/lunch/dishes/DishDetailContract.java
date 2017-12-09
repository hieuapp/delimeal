package com.hieuapp.lunch.dishes;

import android.content.Context;

import com.hieuapp.lunch.BasePresenter;
import com.hieuapp.lunch.BaseView;
import com.hieuapp.lunch.data.Dish;
import com.hieuapp.lunch.data.DishDetail;

import java.util.List;
import java.util.Map;

/**
 * Created by hieuapp on 21/04/2017.
 */

public interface DishDetailContract {
    interface View extends BaseView<Presenter>{
        void showDishDetail(DishDetail dishDetail);

        void adaptDishRelates(List<Dish> dishes);

        void showWaitLoadRelates();
    }

    interface Presenter extends BasePresenter{
        void getDishDetail(int dishId);

        void directions(Context context, DishDetail dish);

        void share(Context context, DishDetail dish);

        void save(Context context, DishDetail dish);

        void doCritique(Map<String, String> session);

        void storeSession(DishDetail dish);
    }
}
