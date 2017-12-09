package com.hieuapp.lunch.util;

import android.content.Context;
import android.location.Location;

import com.hieuapp.lunch.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by hieuapp on 20/04/2017.
 */

public class FormatUtils {
    public static final String DOLA = "$";
    public static String friendlyPrice(float price){
        if(price <= 0){
            return "";
        }
        int iPrice = (int) price;
        String sPrice = String.valueOf(iPrice);
        if(sPrice.length() > 3){
            int dotIndex = sPrice.length() - 3;
            return String.format(DOLA+"%s", sPrice.substring(0, dotIndex) + "." + sPrice.substring(dotIndex));
        }

        return String.format(DOLA+"%s", price);
    }

    public static String friendlyRating(float rating){
        DecimalFormat decimalFormat = new DecimalFormat("#.0");
        return String.valueOf(decimalFormat.format(rating));
    }

    public static String friendlyDistance(double distance){
        DecimalFormat decimalFormat;
        if(distance < 1){
            decimalFormat = new DecimalFormat("0.0");
        }else {
            decimalFormat = new DecimalFormat("#.0");
        }

        return String.format("%s km" ,decimalFormat.format(distance));
    }

    public static String int2Hour(int time){
        int hour = 0;
        int minutes = 0;
        DecimalFormat decimalFormat = new DecimalFormat("##");
        if(time > 60){
            hour = time / 60;
        }

        minutes = time % 60;


        return decimalFormat.format(hour) + ":" + decimalFormat.format(minutes);
    }

    public static String b2String(Context context, int b){
        if(b == 1){
            return context.getResources().getString(R.string.yes);
        }else {
            return context.getResources().getString(R.string.no);
        }
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit.equals("K")) {
            dist = dist * 1.609344;
        } else if (unit.equals("N")) {
            dist = dist * 0.8684;
        }

        return (dist);
    }

    public static boolean validateIPv4(String ip){
        Pattern PATTERN = Pattern.compile(
                "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
        return PATTERN.matcher(ip).matches();
    }

    public static String getHostURL(Context context){

        return "https://api.myjson.com/bins/";

    }

    public static String buildAvatarFBUrl(Context context){
        String uid = AccountUtils.getFacebookId(context);
        return "https://graph.facebook.com/"+uid+"/picture?width=80&height=80";
    }

    public static float distance(double lat1, double lon1, double lat2, double lon2) {
        float[] results = new float[1];
        Location.distanceBetween(lat1, lon1, lat2, lon2, results);

        if(results.length == 1){
            return results[0];
        }else if(results.length == 2){
            return results[1];
        }else if(results.length >= 3){
            return results[2];
        }else {
            return -1;
        }
    }

    /**
     * This function converts decimal degrees to radians
     * @param deg
     * @return
     */
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /**
     * This function converts radians to decimal degrees
     * @param rad
     * @return
     */
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    public static String getCurrentTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        Date date = new Date();
        return sdf.format(date);
    }
}
