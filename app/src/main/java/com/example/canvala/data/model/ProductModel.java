package com.example.canvala.data.model;

import com.example.canvala.util.Constants;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductModel implements Serializable {
    @SerializedName("id_product")
    private String id_product;

    @SerializedName("product_name")
    private String product_name;

    @SerializedName("unit")
    private String unit;

    @SerializedName("price")
    private Integer price;

    @SerializedName("descriptions")
    private String descriptions;

    @SerializedName("category_id")
    private String category_id;

    @SerializedName("stock")
    private Integer stock;

    @SerializedName("id_gallery")
    private String id_gallery;

    @SerializedName("photos")
    private String photos;

    @SerializedName("product_id")
    private String product_id;

    public ProductModel(String id_product, String product_name, String unit, Integer price, String descriptions, String category_id, Integer stock, String id_gallery, String photos, String product_id) {
        this.id_product = id_product;
        this.product_name = product_name;
        this.unit = unit;
        this.price = price;
        this.descriptions = descriptions;
        this.category_id = category_id;
        this.stock = stock;
        this.id_gallery = id_gallery;
        this.photos = photos;
        this.product_id = product_id;
    }

    public String getId_product() {
        return id_product;
    }

    public void setId_product(String id_product) {
        this.id_product = id_product;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getId_gallery() {
        return id_gallery;
    }

    public void setId_gallery(String id_gallery) {
        this.id_gallery = id_gallery;
    }

    public String getPhotos() {
        return Constants.URL_PRODUCT + photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
}
