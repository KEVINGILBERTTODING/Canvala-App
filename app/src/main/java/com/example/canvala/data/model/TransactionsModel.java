package com.example.canvala.data.model;

import com.example.canvala.data.api.ApiConfig;
import com.example.canvala.util.Constants;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TransactionsModel implements Serializable {


        @SerializedName("id_transaction")
        private String transactionId;

        @SerializedName("user_id")
        private String userId;

        @SerializedName("total_price")
        private String totalPrice;

        @SerializedName("city")
        private String city;

        @SerializedName("rekening_id")
        private String rekeningId;

        @SerializedName("transaction_status")
        private String transactionStatus;

        @SerializedName("weight_total")
        private String weightTotal;

        @SerializedName("delivered")
        private String delivered;

        @SerializedName("photo_transaction")
        private String photoTransaction;

        @SerializedName("code")
        private String code;

        @SerializedName("receiver")
        private String receiver;

        @SerializedName("time_arrived")
        private String timeArrived;

        @SerializedName("created_at")
        private String createdAt;

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
        @SerializedName("bank_name")
    private String bankName;

    @SerializedName("number")
    private String noRek;
    @SerializedName("rekening_name")
    private String rekName;


        // Getters and setters


    public TransactionsModel(String transactionId, String userId, String totalPrice, String city, String rekeningId, String transactionStatus, String weightTotal, String delivered, String photoTransaction, String code, String receiver, String timeArrived, String createdAt, String name, String email, String password, String address, String phoneNumber, String postalCode, String roles, String bankName, String noRek, String rekName) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.city = city;
        this.rekeningId = rekeningId;
        this.transactionStatus = transactionStatus;
        this.weightTotal = weightTotal;
        this.delivered = delivered;
        this.photoTransaction = photoTransaction;
        this.code = code;
        this.receiver = receiver;
        this.timeArrived = timeArrived;
        this.createdAt = createdAt;
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.postalCode = postalCode;
        this.roles = roles;
        this.bankName = bankName;
        this.noRek = noRek;
        this.rekName = rekName;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRekeningId() {
        return rekeningId;
    }

    public void setRekeningId(String rekeningId) {
        this.rekeningId = rekeningId;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getWeightTotal() {
        return weightTotal;
    }

    public void setWeightTotal(String weightTotal) {
        this.weightTotal = weightTotal;
    }

    public String getDelivered() {
        return delivered;
    }

    public void setDelivered(String delivered) {
        this.delivered = delivered;
    }

    public String getPhotoTransaction() {
        return  photoTransaction;
    }

    public void setPhotoTransaction(String photoTransaction) {
        this.photoTransaction = photoTransaction;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getTimeArrived() {
        return timeArrived;
    }

    public void setTimeArrived(String timeArrived) {
        this.timeArrived = timeArrived;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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


    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getNoRek() {
        return noRek;
    }

    public void setNoRek(String noRek) {
        this.noRek = noRek;
    }

    public String getRekName() {
        return rekName;
    }

    public void setRekName(String rekName) {
        this.rekName = rekName;
    }
}
