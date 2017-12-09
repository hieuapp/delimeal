package com.hieuapp.lunch.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.DisplayMetrics;

import com.hieuapp.lunch.R;

import static com.hieuapp.lunch.util.LogUtils.LOGD;

/**
 * Created by hieuapp on 07/05/2017.
 */

public class UIUtils {

    private static final int WEIGHT_THUMBNAIL = 224;

    private static final int[] RES_IDS_ACTION_BAR_SIZE = { R.attr.actionBarSize };

    /** Calculates the Action Bar height in pixels. */
    public static int calculateActionBarSize(Context context) {
        if (context == null) {
            return 0;
        }

        Resources.Theme curTheme = context.getTheme();
        if (curTheme == null) {
            return 0;
        }

        TypedArray att = curTheme.obtainStyledAttributes(RES_IDS_ACTION_BAR_SIZE);
        if (att == null) {
            return 0;
        }

        float size = att.getDimension(0, 0);
        att.recycle();
        return (int) size;
    }

    public static int calculateNumColumns (Activity context){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int dp =  Math.round(width / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        int widthThumbnail = context.getResources().getInteger(R.integer.image_thumbnail_width);
        LOGD("width dp = ", ""+dp);
        return dp / widthThumbnail;
    }
}
