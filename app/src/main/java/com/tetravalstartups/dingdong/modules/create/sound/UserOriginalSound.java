package com.tetravalstartups.dingdong.modules.create.sound;

public class UserOriginalSound {
    private String id;
    private String title;
    private String user_id;
    private String user_handle;

    public UserOriginalSound() {
    }

    public UserOriginalSound(String id, String title, String user_id, String user_handle) {
        this.id = id;
        this.title = title;
        this.user_id = user_id;
        this.user_handle = user_handle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_handle() {
        return user_handle;
    }

    public void setUser_handle(String user_handle) {
        this.user_handle = user_handle;
    }
}
