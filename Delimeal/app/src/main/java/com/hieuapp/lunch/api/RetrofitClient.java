package com.hieuapp.lunch.api;

import com.hieuapp.lunch.util.LunchConfig;

import java.io.Serializable;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hieuapp on 18/04/2017.
 */

public class RetrofitClient implements Serializable {
    private static Retrofit retrofit;

    private RetrofitClient(){
        if(retrofit != null){
            throw new RuntimeException("Let use getInstance() instead of constructor ");
        }
    }

    public static Retrofit getInstance(String url){
        if(retrofit == null){
            synchronized (RetrofitClient.class){
                if(retrofit == null){
                    retrofit = new Retrofit.Builder()
                            .baseUrl(url)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }

        return retrofit;
    }
}
