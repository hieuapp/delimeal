package com.hieuapp.lunch.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.hieuapp.lunch.api.RegisterResp;
import com.hieuapp.lunch.api.RetrofitClient;
import com.hieuapp.lunch.api.UserAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hieuapp.lunch.util.LogUtils.LOGD;
import static com.hieuapp.lunch.util.LogUtils.makeLogTag;

/**
 * Created by hieuapp on 06/04/2017.
 */

public class AccountUtils {

    public static final String TAG = makeLogTag(AccountUtils.class);

    private static final String PREF_FACEBOOK_ACCOUNT = "facebook_account";

    private static final String PREF_USER_PROFILE = "user_profile";
    private static final String ACCOUNT_ACTIVE = "account_active";

    public static final String USER_AVATAR = "avatar";
    public static final String USER_NAME = "name";
    public static final String USER_EMAIL = "email";

    private static SharedPreferences getSharedPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static boolean saveFacebookId(final Context context, final String fid) {
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putString(PREF_FACEBOOK_ACCOUNT, fid).apply();
        return true;
    }

    public static String getFacebookId(Context context){
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getString(PREF_FACEBOOK_ACCOUNT, "");
    }

    public static void setAccountActive(Context context, boolean isActive){
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putBoolean(ACCOUNT_ACTIVE, isActive).apply();
    }

    public static boolean isAccountActive(Context context){
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getBoolean(ACCOUNT_ACTIVE, false);
    }
    public static JSONObject getUserProfile(Context context){
        JSONObject user = null;
        try{
            SharedPreferences sp = getSharedPreferences(context);
            String json = sp.getString(PREF_USER_PROFILE, "");
            if(!json.equals("")){
                user = new JSONObject(json);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }

    public static boolean setUserValue(Context context, String key, String value){
        if(key == null || key.equals("")){
            return false;
        }

        try{
            SharedPreferences sp = getSharedPreferences(context);
            JSONObject user = getUserProfile(context);
            if(user == null){
                user = new JSONObject();
            }
            user.put(key, value);
            sp.edit().putString(PREF_USER_PROFILE, user.toString()).apply();
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void register(final Context context, String token){

        String baseURL = FormatUtils.getHostURL(context);
        UserAPI userAPI = RetrofitClient.getInstance(baseURL).create(UserAPI.class);
        Map<String, String> data = new HashMap<>();
        data.put("token", token);
        Call<RegisterResp> call = userAPI.register(data);
        call.enqueue(new Callback<RegisterResp>() {
            @Override
            public void onResponse(Call<RegisterResp> call, Response<RegisterResp> response) {
                if(response.body() ==  null){
                    AccountUtils.setAccountActive(context, false);
                    return;
                }

                boolean registered = response.body().isRegistered();
                AccountUtils.setAccountActive(context, registered);
            }

            @Override
            public void onFailure(Call<RegisterResp> call, Throwable t) {
                AccountUtils.setAccountActive(context, false);
                Toast.makeText(context, "Register err: "+t.getCause().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
