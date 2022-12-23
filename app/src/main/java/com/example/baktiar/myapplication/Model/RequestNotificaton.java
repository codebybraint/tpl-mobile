package com.example.baktiar.myapplication.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Baktiar Krisna on 03/07/2018.
 */

public class RequestNotificaton {

    @SerializedName("to")
    private String token;

    @SerializedName("notification")
    private SendNotificationModel sendNotificationModel;

    public SendNotificationModel getSendNotificationModel() {
        return sendNotificationModel;
    }

    public void setSendNotificationModel(SendNotificationModel sendNotificationModel) {
        this.sendNotificationModel = sendNotificationModel;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}