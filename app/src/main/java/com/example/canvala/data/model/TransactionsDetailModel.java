package com.example.canvala.data.model;

import com.example.canvala.util.Constants;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TransactionsDetailModel implements Serializable {
    @SerializedName("")
    private String emptySerializedName;

    @SerializedName("id_transaction_detail")
    private String transactionDetailId;

    @SerializedName("transaction_id")
    private String transactionId;

    @SerializedName("product_id")
    private String productId;

    @SerializedName("price")
    private String price;

    @SerializedName("banyak")
    private String banyak;

    @SerializedName("code_product")
    private String codeProduct;

    @SerializedName("product_name")
    private String productName;

    @SerializedName("unit")
    private String unit;

    @SerializedName("descriptions")
    private String descriptions;

    @SerializedName("category_id")
    private String categoryId;

    @SerializedName("stock")
    private String stock;

    @SerializedName("id_gallery")
    private String galleryId;

    @SerializedName("photos")
    private String photos;

    public TransactionsDetailModel(String emptySerializedName, String transactionDetailId, String transactionId, String productId, String price, String banyak, String codeProduct, String productName, String unit, String descriptions, String categoryId, String stock, String galleryId, String photos) {
        this.emptySerializedName = emptySerializedName;
        this.transactionDetailId = transactionDetailId;
        this.transactionId = transactionId;
        this.productId = productId;
        this.price = price;
        this.banyak = banyak;
        this.codeProduct = codeProduct;
        this.productName = productName;
        this.unit = unit;
        this.descriptions = descriptions;
        this.categoryId = categoryId;
        this.stock = stock;
        this.galleryId = galleryId;
        this.photos = photos;
    }

    public String getEmptySerializedName() {
        return emptySerializedName;
    }

    public void setEmptySerializedName(String emptySerializedName) {
        this.emptySerializedName = emptySerializedName;
    }

    public String getTransactionDetailId() {
        return transactionDetailId;
    }

    public void setTransactionDetailId(String transactionDetailId) {
        this.transactionDetailId = transactionDetailId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBanyak() {
        return banyak;
    }

    public void setBanyak(String banyak) {
        this.banyak = banyak;
    }

    public String getCodeProduct() {
        return codeProduct;
    }

    public void setCodeProduct(String codeProduct) {
        this.codeProduct = codeProduct;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getGalleryId() {
        return galleryId;
    }

    public void setGalleryId(String galleryId) {
        this.galleryId = galleryId;
    }

    public String getPhotos() {
        return Constants.URL_PRODUCT + photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }
}
