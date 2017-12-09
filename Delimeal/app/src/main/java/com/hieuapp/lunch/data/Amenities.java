package com.hieuapp.lunch.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hieuapp on 27/04/2017.
 */

public class Amenities {
    @SerializedName("wifi")
    private int wifi;
    @SerializedName("takeWay")
    private int takeWay;
    @SerializedName("parking")
    private int parking;
    @SerializedName("outSeat")
    private int outSeat;
    @SerializedName("airConditioner")
    private int airConditioner;
    @SerializedName("smokingZone")
    private int smokingZone;
    @SerializedName("manyFloor")
    private int manyFloor;
    @SerializedName("delivery")
    private int delivery;
    @SerializedName("celebrate")
    private int celebrate;

    public int getWifi() {
        return wifi;
    }

    public void setWifi(int wifi) {
        this.wifi = wifi;
    }

    public int getTakeWay() {
        return takeWay;
    }

    public void setTakeWay(int takeWay) {
        this.takeWay = takeWay;
    }

    public int getParking() {
        return parking;
    }

    public void setParking(int parking) {
        this.parking = parking;
    }

    public int getOutSeat() {
        return outSeat;
    }

    public void setOutSeat(int outSeat) {
        this.outSeat = outSeat;
    }

    public int getAirConditioner() {
        return airConditioner;
    }

    public void setAirConditioner(int airConditioner) {
        this.airConditioner = airConditioner;
    }

    public int getSmokingZone() {
        return smokingZone;
    }

    public void setSmokingZone(int smokingZone) {
        this.smokingZone = smokingZone;
    }

    public int getManyFloor() {
        return manyFloor;
    }

    public void setManyFloor(int manyFloor) {
        this.manyFloor = manyFloor;
    }

    public int getDelivery() {
        return delivery;
    }

    public void setDelivery(int delivery) {
        this.delivery = delivery;
    }

    public int getCelebrate() {
        return celebrate;
    }

    public void setCelebrate(int celebrate) {
        this.celebrate = celebrate;
    }
}
