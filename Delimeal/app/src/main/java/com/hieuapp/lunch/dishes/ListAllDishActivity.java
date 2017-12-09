package com.hieuapp.lunch.dishes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.hieuapp.lunch.LunchIOActivity;
import com.hieuapp.lunch.R;
import com.hieuapp.lunch.data.Dish;
import com.hieuapp.lunch.data.DishCategory;
import com.hieuapp.lunch.data.source.DishRepository;
import com.hieuapp.lunch.data.source.local.DishLocalData;
import com.hieuapp.lunch.data.source.remote.DishRemoteData;
import com.hieuapp.lunch.util.AccountUtils;
import com.hieuapp.lunch.util.Mapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hieuapp on 05/05/2017.
 */

public class ListAllDishActivity extends AppCompatActivity implements DishesContract.View{
    private List<Dish> dishList = new ArrayList<>();;
    private DishesContract.Presenter mPresenter;
    CircularProgressView progressView;
    RecyclerView recyclerView;
    DishesAdapter adapter;


    @Override
    public void showListDish(List<Dish> dishes) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            int category = bundle.getInt("category");
            if(category <= 0){
                finish();
            }
        }

        setContentView(R.layout.all_dish_act);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_saved);
        progressView = (CircularProgressView)findViewById(R.id.load_dish_progress);

        adapter = new DishesAdapter(this, dishList, DishesAdapter.CARD_VIEW_MODE);
        recyclerView.setAdapter(adapter);

        initPresenter();
        mPresenter.loadDishes(false);
        progressView.setVisibility(View.VISIBLE);
        progressView.startAnimation();
    }

    private void initPresenter() {
        DishRemoteData dishRemoteData = DishRemoteData.getInstance(this);
        DishLocalData dishLocalData = DishLocalData.getInstance(this);
        DishRepository dishRepository = DishRepository.getInstance(dishRemoteData, dishLocalData);

        mPresenter = new DishesPresenter(dishRepository, this);
    }

    @Override
    public void showCategories(List<DishCategory> categories) {
        progressView.stopAnimation();
        progressView.setVisibility(View.GONE);
        if (categories == null) {
            //TODO show message load data failed
            Toast.makeText(this, "Cannot load data", Toast.LENGTH_LONG).show();
            return;
        }
        DishCategory category = categories.get(0);
        setTitle(category.getCategoryName());
        List<Dish> dishes = category.getDishes();
        for(Dish dish : dishes){
            dishList.add(dish);
        }
        adapter.notifyDataSetChanged();
    }
}
