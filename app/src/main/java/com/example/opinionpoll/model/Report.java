package com.example.opinionpoll.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Report implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productName);
        dest.writeDouble(this.productAverage);
    }

    public void readFromParcel(Parcel source) {
        this.productName = source.readString();
        this.productAverage = source.readDouble();
    }

    protected Report(Parcel in) {
        this.productName = in.readString();
        this.productAverage = in.readDouble();
    }

    public static final Parcelable.Creator<Report> CREATOR = new Parcelable.Creator<Report>() {
        @Override
        public Report createFromParcel(Parcel source) {
            return new Report(source);
        }

        @Override
        public Report[] newArray(int size) {
            return new Report[size];
        }
    };
}
