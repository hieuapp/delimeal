package com.hieuapp.lunch.dishes;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.hieuapp.lunch.LunchIOActivity;
import com.hieuapp.lunch.api.DishResponse;
import com.hieuapp.lunch.api.LunchAPI;
import com.hieuapp.lunch.api.RetrofitClient;
import com.hieuapp.lunch.data.Amenities;
import com.hieuapp.lunch.data.Dish;
import com.hieuapp.lunch.data.DishDetail;
import com.hieuapp.lunch.data.source.DataSource;
import com.hieuapp.lunch.data.source.DishRepository;
import com.hieuapp.lunch.util.FormatUtils;
import com.hieuapp.lunch.util.Mapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hieuapp on 21/04/2017.
 */

public class DishDetailPresenter extends Mapping implements DishDetailContract.Presenter {
    private final DishRepository dishModel;

    private final DishDetailContract.View dishView;
    private LunchAPI dishService;

    public DishDetailPresenter(DishRepository repository, DishDetailActivity view) {
        this.dishModel = repository;
        this.dishView = view;
        String baseURL = FormatUtils.getHostURL(view);
        this.dishService = RetrofitClient.getInstance(baseURL).create(LunchAPI.class);
    }

    @Override
    public void storeSession(DishDetail dish) {
        LunchIOActivity.session.put(CATEGORY_KEY, String.valueOf(dish.getCategory()));
        LunchIOActivity.session.put(PRICE_KEY, String.valueOf(dish.getPrice()));
        LunchIOActivity.session.put(RATING_KEY,String.valueOf(dish.getRating()));
        LunchIOActivity.session.put(WIFI_KEY,String.valueOf(dish.getAmenities().getWifi()));
        LunchIOActivity.session.put(TAKEWAY_KEY, String.valueOf(dish.getAmenities().getTakeWay()));
        LunchIOActivity.session.put(PARKING_KEY, String.valueOf(dish.getAmenities().getParking()));
        LunchIOActivity.session.put(OUTDOOR_SEAT_KEY, String.valueOf(dish.getAmenities().getOutSeat()));
        LunchIOActivity.session.put(DELIVERY_KEY, String.valueOf(dish.getAmenities().getDelivery()));
        LunchIOActivity.session.put(AIR_KEY, String.valueOf(dish.getAmenities().getAirConditioner()));
        LunchIOActivity.session.put(SMOKING_KEY, String.valueOf(dish.getAmenities().getSmokingZone()));
        LunchIOActivity.session.put(CELEBRATE_KEY, String.valueOf(dish.getAmenities().getCelebrate()));
        LunchIOActivity.session.put(FLOOR_KEY, String.valueOf(dish.getAmenities().getManyFloor()));
    }

    @Override
    public void getDishDetail(int dishId) {
        dishModel.getOnce(dishId, new DataSource.LoadDataListener<DishDetail>() {
            @Override
            public void onDataLoaded(DishDetail data) {
                if(data != null){
                    data.setMapAmenities(amenities2Map(data.getAmenities()));
                    dishView.showDishDetail(data);
                }
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    private Map<String, Integer> amenities2Map(Amenities amenities){
        Map<String, Integer> map = new HashMap<>();

        if(amenities == null){
            return map;
        }

        map.put(WIFI_KEY, amenities.getWifi());
        map.put(TAKEWAY_KEY, amenities.getTakeWay());
        map.put(AIR_KEY, amenities.getAirConditioner());
        map.put(DELIVERY_KEY, amenities.getDelivery());
        map.put(SMOKING_KEY, amenities.getSmokingZone());
        map.put(OUTDOOR_SEAT_KEY, amenities.getOutSeat());
        map.put(CELEBRATE_KEY, amenities.getCelebrate());
        map.put(FLOOR_KEY, amenities.getManyFloor());
        map.put(PARKING_KEY, amenities.getParking());

        return map;

    }

    @Override
    public void directions(Context context, DishDetail dish) {
        Uri directUri = Uri.parse("http://maps.google.com/maps?saddr=" +
                ExploreDishesFragment.getLocation().getLatitude()+ "," +
                ExploreDishesFragment.getLocation().getLongitude()+ "&daddr=" +
                dish.getLatitude()+","+dish.getLongitude());

        Intent intent = new Intent(Intent.ACTION_VIEW, directUri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_LAUNCHER );
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        context.startActivity(intent);
    }

    @Override
    public void share(Context context, DishDetail dish) {
        String message = dish.getFoodName() + "\n" +
                "Restaurant: " + dish.getRestName() + "\n" +
                "Address: " + dish.getFullAddress();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, dish.getFoodName());
        intent.putExtra(Intent.EXTRA_TEXT, message);
        context.startActivity(Intent.createChooser(intent, "Share via"));
    }

    @Override
    public void save(Context context, DishDetail dish) {

    }

    @Override
    public void doCritique(Map<String, String> session) {
        dishView.showWaitLoadRelates();

        Call<DishResponse> call = dishService.doCritique();

        call.enqueue(new Callback<DishResponse>() {
            @Override
            public void onResponse(Call<DishResponse> call, Response<DishResponse> response) {
                if(response.body() == null){
                    return;
                }
                List<Dish> dishes = response.body().getDishes();
                dishView.adaptDishRelates(dishes);
            }

            @Override
            public void onFailure(Call<DishResponse> call, Throwable t) {
                Log.e("DishDetailPresenter", t.getMessage());
            }
        });
    }

    @Override
    public void start() {

    }
}
