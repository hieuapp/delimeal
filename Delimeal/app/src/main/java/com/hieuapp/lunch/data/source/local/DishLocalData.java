package com.hieuapp.lunch.data.source.local;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hieuapp.lunch.data.source.DataSource;

/**
 * Created by hieuapp on 26/02/2017.
 */

public class DishLocalData implements DataSource {
    @Override
    public void getOnce(int id, LoadDataListener loadDataListener) {

    }

    private static DishLocalData localDataInstance;

    public static DishLocalData getInstance(Context context){
        if(localDataInstance == null){
            localDataInstance = new DishLocalData(context);
        }

        return localDataInstance;
    }

    public DishLocalData(@NonNull Context context){
        //TODO init DishesDBHelper
    }
    @Override
    public void getAllItems(@NonNull LoadDataListener loadCallback) {
    }
}
