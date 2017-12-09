package com.hieuapp.lunch.api;

import com.hieuapp.lunch.data.Comment;
import com.hieuapp.lunch.data.Dish;
import com.hieuapp.lunch.data.DishDetail;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by hieuapp on 06/04/2017.
 */

public interface LunchAPI {

    @GET("balpj")
    Call<DishResponse> getDishRecommends();

    @GET("17yh9n")
    Call<DishDetail> getDish();

    @GET("balpj")
    Call<DishResponse> doCritique();

    @GET("dishes/pair")
    Call<DishPair> getPair(@Query("d1") String id1, @Query("d2") String id2);

    @GET("73qx7")
    Call<List<Dish>> getDishSaved();

    @FormUrlEncoded
    @PUT("dishes/favorite")
    Call<Object> save(@FieldMap Map<String, String> data);

    @FormUrlEncoded
    @PUT("dishes/favorite/unsave")
    Call<Object> delete(@FieldMap Map<String, String> data);

    @GET("73qx7")
    Call<List<Dish>> search();

    @FormUrlEncoded
    @POST("dishes/comments")
    Call<Comment> comment(@FieldMap Map<String, String> data);

    @GET("dishes/my-favorite")
    Call<String> checkSaved(@Query("uid") String uid, @Query("dish") String dish);
}
