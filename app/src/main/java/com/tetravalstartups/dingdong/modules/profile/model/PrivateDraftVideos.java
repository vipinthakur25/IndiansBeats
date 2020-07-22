package com.tetravalstartups.dingdong.modules.profile.model;

public class PrivateDraftVideos {
    private String id;
    private String views;
    private String thumbnail;

    public PrivateDraftVideos() {
    }

    public PrivateDraftVideos(String id, String views, String thumbnail) {
        this.id = id;
        this.views = views;
        this.thumbnail = thumbnail;
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
}
