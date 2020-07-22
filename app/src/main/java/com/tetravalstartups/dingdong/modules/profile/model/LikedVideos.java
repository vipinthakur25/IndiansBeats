package com.tetravalstartups.dingdong.modules.profile.model;

public class LikedVideos {
    private String id;
    private String views;
    private String thumbnail;
    private String user_id;

    public LikedVideos() {
    }

    public LikedVideos(String id, String views, String thumbnail, String user_id) {
        this.id = id;
        this.views = views;
        this.thumbnail = thumbnail;
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
