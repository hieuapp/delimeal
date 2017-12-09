package com.hieuapp.lunch.welcome;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hieuapp.lunch.R;

import static com.hieuapp.lunch.util.LogUtils.makeLogTag;

/**
 * Created by hieuapp on 06/04/2017.
 */

public class IntroFragment extends Fragment implements WelcomeActivity.WelcomeContent{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_intro, container, false);

        return root;
    }


    @Override
    public boolean shouldDisplay(Context context) {
        return false;
    }
}
