package com.hieuapp.lunch.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by hieuapp on 28/04/2017.
 */

public class LunchConfig {

    public static final int MAX_RECMD_DISH = 60;
    public static final String IP_HOST = "192.168.4.101";
    private static final String HOST_ADDRESS = "host_ip";


    private static SharedPreferences getSharedPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setIpHost(Context context, String ip){
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putString(HOST_ADDRESS, ip).apply();
    }

    public static String getIpHost(Context context){
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getString(HOST_ADDRESS, "");
    }
}
