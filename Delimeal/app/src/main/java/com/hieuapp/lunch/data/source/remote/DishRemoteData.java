package com.hieuapp.lunch.data.source.remote;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hieuapp.lunch.api.DishResponse;
import com.hieuapp.lunch.api.LunchAPI;
import com.hieuapp.lunch.api.RetrofitClient;
import com.hieuapp.lunch.data.Dish;
import com.hieuapp.lunch.data.DishCategory;
import com.hieuapp.lunch.data.DishDetail;
import com.hieuapp.lunch.data.source.DataSource;
import com.hieuapp.lunch.util.FormatUtils;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hieuapp.lunch.util.LogUtils.LOGD;

/**
 * Created by hieuapp on 26/02/2017.
 */

public class DishRemoteData implements DataSource {

    private static DishRemoteData remoteData;

    private LunchAPI lunchAPI;

    private DishRemoteData(Context context){
        if(remoteData != null){
            throw new RuntimeException("Using getInstance() insteadof constructor");
        }
        String baseURL = FormatUtils.getHostURL(context);
        lunchAPI = RetrofitClient.getInstance(baseURL).create(LunchAPI.class);
    }

    public static DishRemoteData getInstance(Context context){
        if(remoteData == null){
            synchronized (DishRemoteData.class){
                if(remoteData == null){
                    remoteData = new DishRemoteData(context);
                }
            }
        }

        return remoteData;
    }

    @Override
    public void getOnce(int id,final LoadDataListener loadDataListener) {
        Call<DishDetail> call = lunchAPI.getDish();
        call.enqueue(new Callback<DishDetail>() {
            @Override
            public void onResponse(Call<DishDetail> call, Response<DishDetail> response) {
                if(response.body() == null){
                    loadDataListener.onDataLoaded(null);
                }else {
                    loadDataListener.onDataLoaded(response.body());
                }
            }

            @Override
            public void onFailure(Call<DishDetail> call, Throwable t) {
                loadDataListener.onDataLoaded(null);
            }
        });

    }

    @Override
    public void getAllItems(@NonNull LoadDataListener loadCallback) {

    }
}
