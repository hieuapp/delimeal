package com.hieuapp.lunch.api;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by hieuapp on 26/04/2017.
 */

public interface UserAPI {

    @FormUrlEncoded
    @POST("users/register")
    Call<RegisterResp> register(@FieldMap Map<String, String> data);
}
