package com.tetravalstartups.dingdong.auth;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@IgnoreExtraProperties
public class Profile {
    @ServerTimestamp
    private Date timestamp;
    private String id;
    private String name;
    private String handle;
    private String photo;
    private String bio;
    private String phone;
    private String email;
    private String likes;
    private String followers;
    private String following;

    public Profile() {
    }

    public Profile(Date timestamp, String id, String name, String handle, String photo, String bio, String phone, String email, String likes, String followers, String following) {
        this.timestamp = timestamp;
        this.id = id;
        this.name = name;
        this.handle = handle;
        this.photo = photo;
        this.bio = bio;
        this.phone = phone;
        this.email = email;
        this.likes = likes;
        this.followers = followers;
        this.following = following;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }
}
