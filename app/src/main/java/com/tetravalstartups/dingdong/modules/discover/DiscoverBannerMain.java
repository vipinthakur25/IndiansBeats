package com.tetravalstartups.dingdong.modules.discover;

import android.os.Parcel;
import android.os.Parcelable;

public class DiscoverBannerMain implements Parcelable {
    private String banner_id;
    private String banner_url;
    private String banner_tag;

    public DiscoverBannerMain() {
    }

    public DiscoverBannerMain(String banner_id, String banner_url, String banner_tag) {
        this.banner_id = banner_id;
        this.banner_url = banner_url;
        this.banner_tag = banner_tag;
    }

    protected DiscoverBannerMain(Parcel in) {
        banner_id = in.readString();
        banner_url = in.readString();
        banner_tag = in.readString();
    }

    public static final Creator<DiscoverBannerMain> CREATOR = new Creator<DiscoverBannerMain>() {
        @Override
        public DiscoverBannerMain createFromParcel(Parcel in) {
            return new DiscoverBannerMain(in);
        }

        @Override
        public DiscoverBannerMain[] newArray(int size) {
            return new DiscoverBannerMain[size];
        }
    };

    public String getBanner_id() {
        return banner_id;
    }

    public void setBanner_id(String banner_id) {
        this.banner_id = banner_id;
    }

    public String getBanner_url() {
        return banner_url;
    }

    public void setBanner_url(String banner_url) {
        this.banner_url = banner_url;
    }

    public String getBanner_tag() {
        return banner_tag;
    }

    public void setBanner_tag(String banner_tag) {
        this.banner_tag = banner_tag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(banner_id);
        dest.writeString(banner_url);
        dest.writeString(banner_tag);
    }
}
