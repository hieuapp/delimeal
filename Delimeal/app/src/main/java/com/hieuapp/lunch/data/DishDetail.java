package com.hieuapp.lunch.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * Created by hieuapp on 21/04/2017.
 */

public class DishDetail {
    @SerializedName("restName")
    private String restName;
    @SerializedName("description")
    private String description;
    @SerializedName("deleted")
    private int deleted;
    @SerializedName("timeOpen")
    private int timeOpen;
    @SerializedName("timeClose")
    private int timeClose;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("fullAddress")
    private String fullAddress;
    @SerializedName("isClose")
    private int isClosed;
    @SerializedName("id")
    private int id;
    @SerializedName("foodName")
    private String foodName;
    @SerializedName("shortAddress")
    private String shortAddress;
    @SerializedName("rating")
    private float rating;
    @SerializedName("price")
    private float price;
    @SerializedName("image")
    private String image;
    @SerializedName("comfort")
    private Amenities amenities;
    @SerializedName("categoryId")
    private int category;
    @SerializedName("isSaved")
    private boolean isSaved;

    @SerializedName("comments")
    private List<Comment> comments;

    private Map<String, Integer> mapAmenities;


    public String getRestName() {
        return restName;
    }

    public void setRestName(String restName) {
        this.restName = restName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public int getTimeOpen() {
        return timeOpen;
    }

    public void setTimeOpen(int timeOpen) {
        this.timeOpen = timeOpen;
    }

    public int getTimeClose() {
        return timeClose;
    }

    public void setTimeClose(int timeClose) {
        this.timeClose = timeClose;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public int getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(int isClosed) {
        this.isClosed = isClosed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getShortAddress() {
        return shortAddress;
    }

    public void setShortAddress(String shortAddress) {
        this.shortAddress = shortAddress;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Amenities getAmenities() {
        return amenities;
    }

    public void setAmenities(Amenities amenities) {
        this.amenities = amenities;
    }

    public Map<String, Integer> getMapAmenities() {
        return mapAmenities;
    }

    public void setMapAmenities(Map<String, Integer> mapAmenities) {
        this.mapAmenities = mapAmenities;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
