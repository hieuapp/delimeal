package com.hieuapp.lunch.dishes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.hieuapp.lunch.api.DishResponse;
import com.hieuapp.lunch.api.LunchAPI;
import com.hieuapp.lunch.api.RetrofitClient;
import com.hieuapp.lunch.data.Dish;
import com.hieuapp.lunch.data.DishCategory;
import com.hieuapp.lunch.data.source.DishRepository;
import com.hieuapp.lunch.map.MarkerHelper;
import com.hieuapp.lunch.util.AccountUtils;
import com.hieuapp.lunch.util.FormatUtils;
import com.hieuapp.lunch.util.LunchConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hieuapp.lunch.util.LogUtils.LOGD;

/**
 * Created by hieuapp on 26/02/2017.
 */

public class DishesPresenter implements DishesContract.Presenter {

    private final DishRepository mDishesDishRepository;

    private final DishesContract.View mDishesView;
//    Map<String, String> contextuals;

    private Context mContext;
    private String baseURL;


    public DishesPresenter(DishRepository dishRepository,
                           DishesContract.View dishesView) {
        this.mDishesDishRepository = dishRepository;
        this.mDishesView = dishesView;
//        this.contextuals = contextuals;

        if(dishesView instanceof Fragment){
            mContext = ((Fragment) dishesView).getContext();
        }else if(dishesView instanceof AppCompatActivity){
            mContext = (Context)dishesView;
        }else {
            throw new RuntimeException("Cannot cast DishesContract.View to Activity or Fragment");
        }

        baseURL = FormatUtils.getHostURL(mContext);
    }

    @Override
    public void loadDishes(boolean forceUpdate) {
        LunchAPI dishService = RetrofitClient.getInstance(baseURL).create(LunchAPI.class);

//        if(contextuals == null){
//            mDishesView.showCategories(null);
//            return;
//        }
//        contextuals.put("start", "0");
//        contextuals.put("size", String.valueOf(LunchConfig.MAX_RECMD_DISH));
//        String uid = contextuals.get("uid");
//        if(uid == null || uid.equals("")){
//            mDishesView.showCategories(null);
//            return;
//        }

        Call<DishResponse> call = dishService.getDishRecommends();
        call.enqueue(new Callback<DishResponse>() {
            @Override
            public void onResponse(Call<DishResponse> call, Response<DishResponse> response) {
                if(response.code() == 200 && response.body() != null){
                    List<Dish> dishes = response.body().getDishes();
                    //Store dist list for using later
                    MarkerHelper.getInstance().storeDishData(dishes);
                    List<DishCategory> categories = sortCategory(dishes) ;
                    mDishesView.showCategories(categories);
                }else {
                    mDishesView.showCategories(null);
                }
            }

            @Override
            public void onFailure(Call<DishResponse> call, Throwable t) {
                mDishesView.showCategories(null);
            }
        });
    }

    @Override
    public void loadDishSaved(String uid){
        LunchAPI dishService = RetrofitClient.getInstance(baseURL).create(LunchAPI.class);
        Call<List<Dish>> call = dishService.getDishSaved();
        call.enqueue(new Callback<List<Dish>>() {
            @Override
            public void onResponse(Call<List<Dish>> call, Response<List<Dish>> response) {
                if(response.body() != null){
                    mDishesView.showListDish(response.body());
                }else {
                    mDishesView.showListDish(new ArrayList<Dish>());
                }
            }

            @Override
            public void onFailure(Call<List<Dish>> call, Throwable t) {
                mDishesView.showListDish(new ArrayList<Dish>());
            }
        });
    }

    public void save(String uid, String dishID){
        LunchAPI dishService = RetrofitClient.getInstance(baseURL).create(LunchAPI.class);
        Map<String, String> data = new HashMap<>();
        data.put("user", uid);
        data.put("dish", dishID);
        Call<Object> call = dishService.save(data);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.body() != null){

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

    }

    private List<DishCategory> sortCategory(List<Dish> dishes){

        Map<String, List<Dish>> mapCategory = new HashMap<>();

        for(Dish dish : dishes){
            String category = dish.getCategory();
            List<Dish> dishList = mapCategory.get(category);
            if(dishList == null){
                dishList = new ArrayList<>();
            }
            dishList.add(dish);
            mapCategory.put(category, dishList);
        }

        List<DishCategory> categories = new ArrayList<>();
        Set<String> keys = mapCategory.keySet();
        for(String category : keys){
            DishCategory dishCategory = new DishCategory();
            dishCategory.setCategoryName(category);
            dishCategory.setDishes(mapCategory.get(category));
            dishCategory.setNumberDishes(mapCategory.get(category).size());

            categories.add(dishCategory);
        }
        return categories;
    }

    @Override
    public void start() {
    }
}
