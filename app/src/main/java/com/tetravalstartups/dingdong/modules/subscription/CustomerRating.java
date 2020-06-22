package com.tetravalstartups.dingdong.modules.subscription;

public class CustomerRating {
    private String id;
    private String photo;
    private String name;
    private float rating;
    private String review;
    private String type;

    public CustomerRating() {
    }

    public CustomerRating(String id, String photo, String name, float rating, String review, String type) {
        this.id = id;
        this.photo = photo;
        this.name = name;
        this.rating = rating;
        this.review = review;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
