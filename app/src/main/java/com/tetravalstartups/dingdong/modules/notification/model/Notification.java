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
    private String sender_user_id;
    private String sender_user_photo;
    private String sender_user_handle;
    private String receiver_user_id;
    private String receiver_user_photo;
    private String video_id;
    private String video_thumbnail;
    private String amount;

    public Notification() {
    }

    public Notification(String id, int type, String sender_user_id, String sender_user_photo, String sender_user_handle, String receiver_user_id, String receiver_user_photo, String video_id, String video_thumbnail, String amount) {
        this.id = id;
        this.type = type;
        this.sender_user_id = sender_user_id;
        this.sender_user_photo = sender_user_photo;
        this.sender_user_handle = sender_user_handle;
        this.receiver_user_id = receiver_user_id;
        this.receiver_user_photo = receiver_user_photo;
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

    public String getSender_user_id() {
        return sender_user_id;
    }

    public void setSender_user_id(String sender_user_id) {
        this.sender_user_id = sender_user_id;
    }

    public String getSender_user_photo() {
        return sender_user_photo;
    }

    public void setSender_user_photo(String sender_user_photo) {
        this.sender_user_photo = sender_user_photo;
    }

    public String getSender_user_handle() {
        return sender_user_handle;
    }

    public void setSender_user_handle(String sender_user_handle) {
        this.sender_user_handle = sender_user_handle;
    }

    public String getReceiver_user_id() {
        return receiver_user_id;
    }

    public void setReceiver_user_id(String receiver_user_id) {
        this.receiver_user_id = receiver_user_id;
    }

    public String getReceiver_user_photo() {
        return receiver_user_photo;
    }

    public void setReceiver_user_photo(String receiver_user_photo) {
        this.receiver_user_photo = receiver_user_photo;
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
