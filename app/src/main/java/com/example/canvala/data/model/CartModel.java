package com.example.canvala.data.model;

import com.example.canvala.util.Constants;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CartModel implements Serializable {
     @SerializedName("id_cart")
     private String cartId;

     @SerializedName("user_id")
     private String userId;

     @SerializedName("product_id")
     private String productId;

     @SerializedName("banyak")
     private String quantity;

     @SerializedName("total")
     private String total;

     @SerializedName("id_product")
     private String idProduct;

     @SerializedName("product_name")
     private String productName;

     @SerializedName("unit")
     private String unit;

     @SerializedName("price")
     private String price;

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

     public CartModel(String cartId, String userId, String productId, String quantity, String total, String idProduct, String productName, String unit, String price, String descriptions, String categoryId, String stock, String galleryId, String photos) {
          this.cartId = cartId;
          this.userId = userId;
          this.productId = productId;
          this.quantity = quantity;
          this.total = total;
          this.idProduct = idProduct;
          this.productName = productName;
          this.unit = unit;
          this.price = price;
          this.descriptions = descriptions;
          this.categoryId = categoryId;
          this.stock = stock;
          this.galleryId = galleryId;
          this.photos = photos;
     }

     public String getCartId() {
          return cartId;
     }

     public void setCartId(String cartId) {
          this.cartId = cartId;
     }

     public String getUserId() {
          return userId;
     }

     public void setUserId(String userId) {
          this.userId = userId;
     }

     public String getProductId() {
          return productId;
     }

     public void setProductId(String productId) {
          this.productId = productId;
     }

     public String getQuantity() {
          return quantity;
     }

     public void setQuantity(String quantity) {
          this.quantity = quantity;
     }

     public String getTotal() {
          return total;
     }

     public void setTotal(String total) {
          this.total = total;
     }

     public String getIdProduct() {
          return idProduct;
     }

     public void setIdProduct(String idProduct) {
          this.idProduct = idProduct;
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

     public String getPrice() {
          return price;
     }

     public void setPrice(String price) {
          this.price = price;
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
