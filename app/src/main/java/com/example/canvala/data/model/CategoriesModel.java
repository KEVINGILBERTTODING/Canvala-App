package com.example.canvala.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CategoriesModel implements Serializable {
    @SerializedName("id")
    private String id;
    @SerializedName("category_name")
    private String categoryName;
    @SerializedName("slug")
    private String slug;


    public CategoriesModel(String id, String categoryName, String slug) {
        this.id = id;
        this.categoryName = categoryName;
        this.slug = slug;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
