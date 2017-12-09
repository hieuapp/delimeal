package com.hieuapp.lunch.welcome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.hieuapp.lunch.R;
import com.hieuapp.lunch.api.RegisterResp;
import com.hieuapp.lunch.api.RetrofitClient;
import com.hieuapp.lunch.api.UserAPI;
import com.hieuapp.lunch.util.AccountUtils;
import com.hieuapp.lunch.util.FacebookUtils;
import com.hieuapp.lunch.util.FormatUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hieuapp.lunch.util.LogUtils.LOGD;
import static com.hieuapp.lunch.util.LogUtils.makeLogTag;

/**
 * Created by hieuapp on 09/03/2017.
 */

public class LoginFragment extends WelcomeFragment implements WelcomeActivity.WelcomeContent{

    private static final String TAG = makeLogTag(LoginFragment.class);

    private LoginButton loginButton;

    private CallbackManager callbackManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);

        loginButton = (LoginButton)root.findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        loginButton.setFragment(this);
        loginButton.setHeight(56);
        loginButton.setTextSize(18);

        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken token = loginResult.getAccessToken();
                if(token != null){
                    LOGD(TAG, "Login successfully, save fid = " + token.getUserId());
                    AccountUtils.saveFacebookId(getContext(), token.getUserId());
                    FacebookUtils.fetchUserProfile(getContext());
//                    AccountUtils.register(getContext(), token.getToken());
                }
                doNext();
            }

            @Override
            public void onCancel() {
                doFinish();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getContext(), "Login Error, please try agian", Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }



    @Override
    public boolean shouldDisplay(Context context) {
        AccessToken token = AccessToken.getCurrentAccessToken();

        if(token == null ||
                (token.getExpires().getTime() < System.currentTimeMillis())){
            return true;
        }

        return false;
    }
}
