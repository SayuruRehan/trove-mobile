package com.example.trove_mobile.notification;

import com.google.gson.annotations.SerializedName;

public class Notification {

    @SerializedName("notificationId")
    private String notificationId;

    @SerializedName("dateTime")
    private String dateTime;

    @SerializedName("title")
    private String title;

    @SerializedName("message")
    private String message;

    @SerializedName("role")
    private String role;

    @SerializedName("userId")
    private String userId;

    // Getters and Setters
    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId){
        this.notificationId = notificationId;
    }

    public String getDateTime(){
        return dateTime;
    }

    public void setDateTime(String dateTime){
        this.dateTime = dateTime;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getRole(){
        return role;
    }

    public void setRole(String role){
        this.role = role;
    }

    public String getUserId(){
        return userId;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }
}
