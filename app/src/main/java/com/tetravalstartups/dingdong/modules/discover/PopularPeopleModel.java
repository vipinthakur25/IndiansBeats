package com.tetravalstartups.dingdong.modules.discover;

public class PopularPeopleModel {
    private int image;
    private String username;
    private String followers_count;
    private String id;

    public PopularPeopleModel(int image, String username, String followers_count, String id) {
        this.image = image;
        this.username = username;
        this.followers_count = followers_count;
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFollowers_count() {
        return followers_count;
    }

    public void setFollowers_count(String followers_count) {
        this.followers_count = followers_count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
