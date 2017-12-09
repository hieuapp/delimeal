package com.hieuapp.lunch.user;

/**
 * Created by hieuapp on 27/04/2017.
 */

public class Session {
    private String uid;
    private double latitude;
    private double longitude;
    private float price;
    private float rating;
    private int category;
    private int feature;
    private String wish;

    /*amenities featureCritique*/
    private int wifi;
    private int takeWay;
    private int parking;
    private int outSeat;
    private int airConditioner;
    private int smokingZone;
    private int manyFloor;
    private int delivery;
    private int celebrate;

    public Session(){
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getFeature() {
        return feature;
    }

    public void setFeature(int feature) {
        this.feature = feature;
    }

    public String getWish() {
        return wish;
    }

    public void setWish(String wish) {
        this.wish = wish;
    }

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
