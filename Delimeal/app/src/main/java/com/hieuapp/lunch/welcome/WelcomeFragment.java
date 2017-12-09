package com.hieuapp.lunch.welcome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hieuapp.lunch.LunchIOActivity;

import static com.hieuapp.lunch.util.LogUtils.LOGD;
import static com.hieuapp.lunch.util.LogUtils.makeLogTag;

/**
 * Created by hieuapp on 06/04/2017.
 */

public abstract class WelcomeFragment extends Fragment {

    private static final String TAG = makeLogTag(WelcomeFragment.class);
    protected Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LOGD(TAG, "Attaching to activity");
        mActivity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        LOGD(TAG, "Creating View");
        return view;
    }

    /**
     * Proceed to the next activity.
     */
    void doNext() {
        LOGD(TAG, "Proceeding to next activity");
        Intent intent = new Intent(mActivity, LunchIOActivity.class);
        startActivity(intent);
        mActivity.finish();
    }

    /**
     * Finish the activity.
     * We're done here.
     */
    void doFinish() {
        LOGD(TAG, "Closing app");
        mActivity.finish();
    }
}
