
package com.tetravalstartups.dingdong.modules.discover;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DiscoverBannerResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("banner_url")
    @Expose
    private String bannerUrl;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("action")
    @Expose
    private String action;

    /**
     * No args constructor for use in serialization
     * 
     */
    public DiscoverBannerResponse() {
    }

    /**
     * 
     * @param bannerUrl
     * @param action
     * @param id
     * @param status
     */
    public DiscoverBannerResponse(Integer id, String bannerUrl, Integer status, String action) {
        super();
        this.id = id;
        this.bannerUrl = bannerUrl;
        this.status = status;
        this.action = action;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

}
