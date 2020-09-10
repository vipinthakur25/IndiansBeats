
package com.tetravalstartups.dingdong.modules.profile.videos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VideoResponseDatum {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("sound_id")
    @Expose
    private String soundId;
    @SerializedName("sound_title")
    @Expose
    private String soundTitle;
    @SerializedName("likes_count")
    @Expose
    private Integer likesCount;
    @SerializedName("share_count")
    @Expose
    private Integer shareCount;
    @SerializedName("comment_count")
    @Expose
    private Integer commentCount;
    @SerializedName("view_count")
    @Expose
    private Integer viewCount;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("video_url")
    @Expose
    private String videoUrl;
    @SerializedName("video_thumbnail")
    @Expose
    private String videoThumbnail;
    @SerializedName("video_status")
    @Expose
    private Integer videoStatus;
    @SerializedName("video_index")
    @Expose
    private String videoIndex;
    @SerializedName("video_desc")
    @Expose
    private String videoDesc;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("user_handle")
    @Expose
    private String userHandle;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_photo")
    @Expose
    private String userPhoto;
    @SerializedName("mylike")
    @Expose
    private Integer mylike;

    public VideoResponseDatum() {
    }

    public VideoResponseDatum(String id, String soundId, String soundTitle, Integer likesCount, Integer shareCount, Integer commentCount, Integer viewCount, String userId, String videoUrl, String videoThumbnail, Integer videoStatus, String videoIndex, String videoDesc, String timestamp, String userHandle, String userName, String userPhoto, Integer mylike) {
        this.id = id;
        this.soundId = soundId;
        this.soundTitle = soundTitle;
        this.likesCount = likesCount;
        this.shareCount = shareCount;
        this.commentCount = commentCount;
        this.viewCount = viewCount;
        this.userId = userId;
        this.videoUrl = videoUrl;
        this.videoThumbnail = videoThumbnail;
        this.videoStatus = videoStatus;
        this.videoIndex = videoIndex;
        this.videoDesc = videoDesc;
        this.timestamp = timestamp;
        this.userHandle = userHandle;
        this.userName = userName;
        this.userPhoto = userPhoto;
        this.mylike = mylike;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSoundId() {
        return soundId;
    }

    public void setSoundId(String soundId) {
        this.soundId = soundId;
    }

    public String getSoundTitle() {
        return soundTitle;
    }

    public void setSoundTitle(String soundTitle) {
        this.soundTitle = soundTitle;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public Integer getShareCount() {
        return shareCount;
    }

    public void setShareCount(Integer shareCount) {
        this.shareCount = shareCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoThumbnail() {
        return videoThumbnail;
    }

    public void setVideoThumbnail(String videoThumbnail) {
        this.videoThumbnail = videoThumbnail;
    }

    public Integer getVideoStatus() {
        return videoStatus;
    }

    public void setVideoStatus(Integer videoStatus) {
        this.videoStatus = videoStatus;
    }

    public String getVideoIndex() {
        return videoIndex;
    }

    public void setVideoIndex(String videoIndex) {
        this.videoIndex = videoIndex;
    }

    public String getVideoDesc() {
        return videoDesc;
    }

    public void setVideoDesc(String videoDesc) {
        this.videoDesc = videoDesc;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserHandle() {
        return userHandle;
    }

    public void setUserHandle(String userHandle) {
        this.userHandle = userHandle;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public Integer getMylike() {
        return mylike;
    }

    public void setMylike(Integer mylike) {
        this.mylike = mylike;
    }
}
