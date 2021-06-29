package com.example.opinionpoll.model;

import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("product_id")
    private int productId;

    @SerializedName("product_name")
    private String productName;

    @SerializedName("product_image_path")
    private String productImagePath;

    public Product() {
    }

    public Product(int productId, String productName, String productImagePath) {
        setProductId(productId);
        setProductName(productName);
        setProductImagePath(productImagePath);
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImagePath() {
        return productImagePath;
    }

    public void setProductImagePath(String productImagePath) {
        this.productImagePath = productImagePath;
    }
}
