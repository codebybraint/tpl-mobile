package com.example.baktiar.myapplication.Model;

/**
 * Created by Baktiar Krisna on 03/07/2018.
 */

public class SendNotificationModel {
    private String body,title;

    public SendNotificationModel(String body, String title) {
        this.body = body;
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
