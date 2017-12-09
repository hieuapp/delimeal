package com.hieuapp.lunch.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.hieuapp.lunch.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static com.hieuapp.lunch.util.LogUtils.LOGD;
import static com.hieuapp.lunch.util.LogUtils.makeLogTag;

/**
 * Created by hieuapp on 06/04/2017.
 */

public class FacebookUtils {
    private static final String TAG = makeLogTag(FacebookUtils.class);

    public static void fetchUserProfile(Context context) {
        String uid = AccountUtils.getFacebookId(context);
        fetchAvatar(context, uid);
        fetchUserData(context);
    }

    private static void fetchName(final Context context, String uid) {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + uid,
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        try {
                            String name = response.getJSONObject().getString("name");
                            AccountUtils.setUserValue(context, AccountUtils.USER_NAME, name);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        LOGD("api response ", response.getJSONObject().toString());
                    }
                }
        ).executeAsync();
    }

    private static void fetchAvatar(final Context context,final String uid){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL avatar = new URL("https://graph.facebook.com/" + uid + "/picture?type=large");
                    Bitmap bitmap = BitmapFactory.decodeStream(avatar.openConnection().getInputStream());
                    String sAvatar = ImageUtils.encodeBase64(bitmap);
                    AccountUtils.setUserValue(context, AccountUtils.USER_AVATAR, sAvatar);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Bitmap imgDef = BitmapFactory.decodeResource(context.getResources(), R.drawable.user_default);
                    String avatarDef = ImageUtils.encodeBase64(imgDef);
                    AccountUtils.setUserValue(context, AccountUtils.USER_AVATAR, avatarDef);
                } catch (IOException e) {
                    e.printStackTrace();
                    Bitmap imgDef = BitmapFactory.decodeResource(context.getResources(), R.drawable.user_default);
                    String avatarDef = ImageUtils.encodeBase64(imgDef);
                    AccountUtils.setUserValue(context, AccountUtils.USER_AVATAR, avatarDef);
                }
            }
        }).start();
    }

    private static void fetchUserData(final Context context){
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String name = object.getString(AccountUtils.USER_NAME);
                            AccountUtils.setUserValue(context, AccountUtils.USER_NAME, name);
                            String email = object.getString(AccountUtils.USER_EMAIL);
                            AccountUtils.setUserValue(context, AccountUtils.USER_EMAIL, email);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

}
