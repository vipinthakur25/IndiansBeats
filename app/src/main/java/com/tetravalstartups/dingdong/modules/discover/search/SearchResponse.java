package com.tetravalstartups.dingdong.modules.discover.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchResponse {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("handle")
    @Expose
    private String handle;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("likes")
    @Expose
    private Integer likes;
    @SerializedName("following")
    @Expose
    private Integer following;
    @SerializedName("followers")
    @Expose
    private Integer followers;
    @SerializedName("videos")
    @Expose
    private Integer videos;
    @SerializedName("bio")
    @Expose
    private String bio;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("myfollow")
    @Expose
    private String myfollow;

    public SearchResponse(String id, String handle, String name, String email, Integer likes, Integer following, Integer followers, Integer videos, String bio, String photo, String timestamp, String myfollow) {
        this.id = id;
        this.handle = handle;
        this.name = name;
        this.email = email;
        this.likes = likes;
        this.following = following;
        this.followers = followers;
        this.videos = videos;
        this.bio = bio;
        this.photo = photo;
        this.timestamp = timestamp;
        this.myfollow = myfollow;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getFollowing() {
        return following;
    }

    public void setFollowing(Integer following) {
        this.following = following;
    }

    public Integer getFollowers() {
        return followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public Integer getVideos() {
        return videos;
    }

    public void setVideos(Integer videos) {
        this.videos = videos;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMyfollow() {
        return myfollow;
    }

    public void setMyfollow(String myfollow) {
        this.myfollow = myfollow;
    }
}
