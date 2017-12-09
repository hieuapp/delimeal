package com.hieuapp.lunch.map;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.hieuapp.lunch.data.Dish;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hieuapp on 30/04/2017.
 */

public class MarkerHelper {

    private static MarkerHelper markerHelper;

    private List<Dish> dishList;

    private MarkerHelper(){
        if(markerHelper != null){
            throw new RuntimeException("Using getInstance() method instead of new constructor");
        }
    }

    public static MarkerHelper getInstance(){
        if(markerHelper == null){
            synchronized (MarkerHelper.class){
                if(markerHelper == null){
                    markerHelper = new MarkerHelper();
                }
            }
        }

        return markerHelper;
    }

    public void storeDishData(List<Dish> dishes){
        this.dishList = dishes;
    }

    public List<Dish> getDishData(){
        return dishList;
    }

    public List<MarkerOptions> getAllMarker(Context context){
        if(dishList == null || dishList.size() == 0){
            return new ArrayList<>();
        }

        List<MarkerOptions> markers = new ArrayList<>();

        for(Dish dish : dishList){
            LatLng potision = new LatLng(dish.getLatitude(), dish.getLongitude());
//            MarkerOptions marker = MapUtils.createLabelMarker(iconGenerator,
//                    String.valueOf(dish.getId()), potision, dish.getFoodName());
            MarkerOptions marker = MapUtils.createPinMarker(context, String.valueOf(dish.getId()), potision);
            markers.add(marker);
        }

        return markers;
    }

}
