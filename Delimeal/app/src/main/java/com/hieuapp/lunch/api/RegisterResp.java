package com.hieuapp.lunch.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hieuapp on 26/04/2017.
 */

public class RegisterResp {
    @SerializedName("registered")
    private boolean registered;

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }
}
