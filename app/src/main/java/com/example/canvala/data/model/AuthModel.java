package com.example.canvala.data.model;

import com.google.gson.annotations.SerializedName;

public class AuthModel {
    @SerializedName("status")
    Integer status;
    @SerializedName("user_id")
    String userId;
    @SerializedName("role")
    String role;
    @SerializedName("message")
    String message;

    public AuthModel(Integer status, String userId, String role, String message) {
        this.status = status;
        this.userId = userId;
        this.role = role;
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
