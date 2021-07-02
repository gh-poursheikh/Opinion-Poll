package com.example.opinionpoll.model;

import com.google.gson.annotations.SerializedName;

public class Report {
    @SerializedName("product_name")
    public String productName;

    @SerializedName("product_average")
    public double productAverage;

    public Report() {
    }

    public Report(String productName, double productAverage) {
        this.productName = productName;
        this.productAverage = productAverage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductAverage() {
        return productAverage;
    }

    public void setProductAverage(double productAverage) {
        this.productAverage = productAverage;
    }
}
