package com.hieuapp.lunch.welcome;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.hieuapp.lunch.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hieuapp on 06/04/2017.
 */

public class WelcomeActivity extends AppCompatActivity{

    WelcomeContent contentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        contentFragment = getCurrentFragment(this);
        if(contentFragment == null){
            finish();
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.welcome_content, (Fragment) contentFragment);
        transaction.commit();
    }

    private static WelcomeContent getCurrentFragment(Context context){
        List<WelcomeContent> welcomeContents = getWelcomeContent();
        for(WelcomeContent fragment : welcomeContents){
            if(fragment.shouldDisplay(context)){
                return fragment;
            }
        }

        return null;
    }

    private static List<WelcomeContent> getWelcomeContent(){
        return new ArrayList<WelcomeContent>(Arrays.asList(
                new LoginFragment(),
                new IntroFragment()
        ));
    }

    /**
     * Whether display WelcomeActivity or not
     * @return true if should display Welcome
     */
    public static boolean shouldDisplay(Context context){
        WelcomeContent fragmentContent = getCurrentFragment(context);
        return fragmentContent != null;
    }

    interface WelcomeContent {
        boolean shouldDisplay(Context context);
    }
}
