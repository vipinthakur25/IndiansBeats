package com.tetravalstartups.dingdong.modules.profile.model;

public class Avatar {
    private String id;
    private String photo;

    public Avatar() {
    }

    public Avatar(String id, String photo) {
        this.id = id;
        this.photo = photo;
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
}
