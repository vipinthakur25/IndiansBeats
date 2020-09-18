package com.tetravalstartups.dingdong.modules.discover;

public class DiscoverBannerModel {
    private int image;
    private String hashtag;
    private String count;

    public DiscoverBannerModel(int image, String hashtag, String count) {
        this.image = image;
        this.hashtag = hashtag;
        this.count = count;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
