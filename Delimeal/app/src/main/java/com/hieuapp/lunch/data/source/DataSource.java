package com.hieuapp.lunch.data.source;

import android.support.annotation.NonNull;

import com.hieuapp.lunch.data.DishCategory;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by hieuapp on 26/02/2017.
 */

public interface DataSource {

    interface LoadDataListener<T> {
        void onDataLoaded(T data);

        void onDataNotAvailable();
    }

    interface OnListLoadedListener<T>{
        void onLoaded(List<T> data);
        void onError(String message);
    }

    void getAllItems(@NonNull LoadDataListener loadCallback);

    void getOnce(int id, LoadDataListener loadDataListener);
}
