package com.example.opinionpoll.model;

import com.google.gson.annotations.SerializedName;

public class Rating {
    @SerializedName("person_id")
    private int personId;

    @SerializedName("product_id")
    private int productId;


    @SerializedName("rating_number")
    private int ratingNumber;

    public Rating() {
    }

    public Rating(int personId, int productId, int ratingNumber) {
        super();
        setPersonId(personId);
        setProductId(productId);
        setRatingNumber(ratingNumber);
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getRatingNumber() {
        return ratingNumber;
    }

    public void setRatingNumber(int ratingNumber) {
        this.ratingNumber = ratingNumber;
    }
}
