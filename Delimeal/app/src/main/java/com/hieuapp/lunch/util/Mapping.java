package com.hieuapp.lunch.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hieuapp on 28/04/2017.
 */

public class Mapping {

    public static final int WIFI = 0;
    public static final int TAKE_WAY = 1;
    public static final int PARKING = 2;
    public static final int OUT_DOOR_SEAT = 3;
    public static final int AIR_COND = 4;
    public static final int SMOKING = 5;
    public static final int FLOOR = 6;
    public static final int DELIVER = 7;
    public static final int CELEBRATE = 8;
    public static final int PRICE = 9;
    public static final int RATING = 10;
    public static final int LOCATION = 11;
    public static final int CATEGORY = 12;

    public static final String WIFI_KEY = "wifi";
    public static final String TAKEWAY_KEY = "take_way";
    public static final String PARKING_KEY = "free_bike_parking";
    public static final String OUTDOOR_SEAT_KEY = "out_door_seat";
    public static final String AIR_KEY = "air_conditioner";
    public static final String SMOKING_KEY = "smoking_zone";
    public static final String FLOOR_KEY = "many_floor";
    public static final String DELIVERY_KEY = "delivery_service";
    public static final String CELEBRATE_KEY = "birthday_celebrate";
    public static final String LOCATION_KEY = "location";
    public static final String RATING_KEY = "rating";
    public static final String PRICE_KEY = "price";
    public static final String LATITUDE_KEY = "latitude";
    public static final String LONGITUDE_KEY = "longitude";
    public static final String CATEGORY_KEY = "category";

    public static final Map<String, Integer> critique;

    static {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put(WIFI_KEY, WIFI);
        map.put(TAKEWAY_KEY, TAKE_WAY);
        map.put(PARKING_KEY, PARKING);
        map.put(OUTDOOR_SEAT_KEY, OUT_DOOR_SEAT);
        map.put(AIR_KEY, AIR_COND);
        map.put(SMOKING_KEY, SMOKING);
        map.put(FLOOR_KEY, FLOOR);
        map.put(DELIVERY_KEY, DELIVER);
        map.put(CELEBRATE_KEY, CELEBRATE);
        map.put(LOCATION_KEY, LOCATION);
        map.put(RATING_KEY, RATING);
        map.put(PRICE_KEY, PRICE);

        critique = Collections.unmodifiableMap(map);
    }
}