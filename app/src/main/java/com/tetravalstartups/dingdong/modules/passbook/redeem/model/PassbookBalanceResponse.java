package com.tetravalstartups.dingdong.modules.passbook.redeem.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PassbookBalanceResponse {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("daily_rewards")
    @Expose
    private Integer dailyRewards;
    @SerializedName("fans_donation")
    @Expose
    private Integer fansDonation;
    @SerializedName("subscription")
    @Expose
    private Integer subscription;
    @SerializedName("time_spent")
    @Expose
    private Integer timeSpent;
    @SerializedName("video_uploads")
    @Expose
    private Integer videoUploads;

    public PassbookBalanceResponse() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getDailyRewards() {
        return dailyRewards;
    }

    public void setDailyRewards(Integer dailyRewards) {
        this.dailyRewards = dailyRewards;
    }

    public Integer getFansDonation() {
        return fansDonation;
    }

    public void setFansDonation(Integer fansDonation) {
        this.fansDonation = fansDonation;
    }

    public Integer getSubscription() {
        return subscription;
    }

    public void setSubscription(Integer subscription) {
        this.subscription = subscription;
    }

    public Integer getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(Integer timeSpent) {
        this.timeSpent = timeSpent;
    }

    public Integer getVideoUploads() {
        return videoUploads;
    }

    public void setVideoUploads(Integer videoUploads) {
        this.videoUploads = videoUploads;
    }
}
