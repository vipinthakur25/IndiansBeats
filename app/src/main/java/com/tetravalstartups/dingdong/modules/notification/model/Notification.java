package com.tetravalstartups.dingdong.modules.notification.model;

public class Notification {

    public static final int NOTIFICATION_TYPE_FOLLOW = 1;
    public static final int NOTIFICATION_TYPE_LIKE = 2;
    public static final int NOTIFICATION_TYPE_MENTION = 3;
    public static final int NOTIFICATION_TYPE_REWARD = 4;
    public static final int NOTIFICATION_TYPE_NOTICE = 5;
    public static final int NOTIFICATION_TYPE_COMMENT = 6;

    private String id;
    private int type;
    private String user_id;
    private String user_photo;
    private String video_id;
    private String video_thumbnail;
    private String amount;

    public Notification() {
    }

    public Notification(String id, int type, String user_id, String user_photo, String video_id, String video_thumbnail, String amount) {
        this.id = id;
        this.type = type;
        this.user_id = user_id;
        this.user_photo = user_photo;
        this.video_id = video_id;
        this.video_thumbnail = video_thumbnail;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public String getVideo_thumbnail() {
        return video_thumbnail;
    }

    public void setVideo_thumbnail(String video_thumbnail) {
        this.video_thumbnail = video_thumbnail;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
