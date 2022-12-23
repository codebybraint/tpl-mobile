package com.example.baktiar.myapplication.Model;

import com.example.baktiar.myapplication.Model.RequestNotificaton;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Baktiar Krisna on 03/07/2018.
 */

public interface ApiInterface {
//    @Headers({"Authorization: key=AIzaSyAZiX4tECscbA5KrXgGqWLyXfQeKxGxons",
//            "Content-Type:application/json"})

    @Headers({"Authorization: key=AAAAkdq0Er4:APA91bELeuSY2RNmzR49bE79hihJB2GjbYk-iP0TNvj5nmt5bKJJlmE3R4h4dGSkDos0OQzuv0ohjZWccO1xT6Xye0CKr7YAdw_OrUAnqiq59BrrLFbNlY4A7Cra5gaTB74k8a7D4up3",
            "Content-Type:application/json"})
    @POST("fcm/send")
    Call<ResponseBody> sendChatNotification(@Body RequestNotificaton requestNotificaton);
}