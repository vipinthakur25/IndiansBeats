package com.tetravalstartups.dingdong.modules.discover;

public class TrendingNowModel {
    private int image;
    private String hashtags;
    private String videoCount;


    public TrendingNowModel(int image, String hashtags, String videoCount) {
        this.image = image;
        this.hashtags = hashtags;
        this.videoCount = videoCount;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getHashtags() {
        return hashtags;
    }

    public void setHashtags(String hashtags) {
        this.hashtags = hashtags;
    }

    public String getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(String videoCount) {
        this.videoCount = videoCount;
    }
}
