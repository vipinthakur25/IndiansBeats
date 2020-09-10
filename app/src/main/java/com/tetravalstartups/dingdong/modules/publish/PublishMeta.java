package com.tetravalstartups.dingdong.modules.publish;

import android.os.Parcel;
import android.os.Parcelable;

public class PublishMeta implements Parcelable {
    private String id;
    private String sound_id;
    private String sound_title;
    private String user_id;
    private String video_path;
    private String video_thumbnail;
    private String video_desc;
    private int video_status;
    private int reward_type;
    private String video_index;
    private String subs_id;
    private int plan_id;
    private int monthly_profit;
    private int total_uploads;

    public PublishMeta() {
    }

    public PublishMeta(String id, String sound_id, String sound_title, String user_id, String video_path, String video_thumbnail, String video_desc, int video_status, int reward_type, String video_index, String subs_id, int plan_id, int monthly_profit, int total_uploads) {
        this.id = id;
        this.sound_id = sound_id;
        this.sound_title = sound_title;
        this.user_id = user_id;
        this.video_path = video_path;
        this.video_thumbnail = video_thumbnail;
        this.video_desc = video_desc;
        this.video_status = video_status;
        this.reward_type = reward_type;
        this.video_index = video_index;
        this.subs_id = subs_id;
        this.plan_id = plan_id;
        this.monthly_profit = monthly_profit;
        this.total_uploads = total_uploads;
    }

    protected PublishMeta(Parcel in) {
        id = in.readString();
        sound_id = in.readString();
        sound_title = in.readString();
        user_id = in.readString();
        video_path = in.readString();
        video_thumbnail = in.readString();
        video_desc = in.readString();
        video_status = in.readInt();
        reward_type = in.readInt();
        video_index = in.readString();
        subs_id = in.readString();
        plan_id = in.readInt();
        monthly_profit = in.readInt();
        total_uploads = in.readInt();
    }

    public static final Creator<PublishMeta> CREATOR = new Creator<PublishMeta>() {
        @Override
        public PublishMeta createFromParcel(Parcel in) {
            return new PublishMeta(in);
        }

        @Override
        public PublishMeta[] newArray(int size) {
            return new PublishMeta[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSound_id() {
        return sound_id;
    }

    public void setSound_id(String sound_id) {
        this.sound_id = sound_id;
    }

    public String getSound_title() {
        return sound_title;
    }

    public void setSound_title(String sound_title) {
        this.sound_title = sound_title;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getVideo_path() {
        return video_path;
    }

    public void setVideo_path(String video_path) {
        this.video_path = video_path;
    }

    public String getVideo_thumbnail() {
        return video_thumbnail;
    }

    public void setVideo_thumbnail(String video_thumbnail) {
        this.video_thumbnail = video_thumbnail;
    }

    public String getVideo_desc() {
        return video_desc;
    }

    public void setVideo_desc(String video_desc) {
        this.video_desc = video_desc;
    }

    public int getVideo_status() {
        return video_status;
    }

    public void setVideo_status(int video_status) {
        this.video_status = video_status;
    }

    public int getReward_type() {
        return reward_type;
    }

    public void setReward_type(int reward_type) {
        this.reward_type = reward_type;
    }

    public String getVideo_index() {
        return video_index;
    }

    public void setVideo_index(String video_index) {
        this.video_index = video_index;
    }

    public String getSubs_id() {
        return subs_id;
    }

    public void setSubs_id(String subs_id) {
        this.subs_id = subs_id;
    }

    public int getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(int plan_id) {
        this.plan_id = plan_id;
    }

    public int getMonthly_profit() {
        return monthly_profit;
    }

    public void setMonthly_profit(int monthly_profit) {
        this.monthly_profit = monthly_profit;
    }

    public int getTotal_uploads() {
        return total_uploads;
    }

    public void setTotal_uploads(int total_uploads) {
        this.total_uploads = total_uploads;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(sound_id);
        parcel.writeString(sound_title);
        parcel.writeString(user_id);
        parcel.writeString(video_path);
        parcel.writeString(video_thumbnail);
        parcel.writeString(video_desc);
        parcel.writeInt(video_status);
        parcel.writeInt(reward_type);
        parcel.writeString(video_index);
        parcel.writeString(subs_id);
        parcel.writeInt(plan_id);
        parcel.writeInt(monthly_profit);
        parcel.writeInt(total_uploads);
    }
}
