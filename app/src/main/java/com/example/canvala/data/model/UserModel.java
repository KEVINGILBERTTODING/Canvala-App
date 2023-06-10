package com.example.canvala.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserModel implements Serializable {


        @SerializedName("id_user")
        private String userId;

        @SerializedName("name")
        private String name;

        @SerializedName("email")
        private String email;

        @SerializedName("password")
        private String password;

        @SerializedName("address")
        private String address;

        @SerializedName("phone_number")
        private String phoneNumber;

        @SerializedName("postal_code")
        private String postalCode;

        @SerializedName("roles")
        private String roles;

        // Constructor, getters, and setters


    public UserModel(String userId, String name, String email, String password, String address, String phoneNumber, String postalCode, String roles) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.postalCode = postalCode;
        this.roles = roles;
    }

    public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getPostalCode() {
            return postalCode;
        }

        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }

        public String getRoles() {
            return roles;
        }

        public void setRoles(String roles) {
            this.roles = roles;
        }





}
