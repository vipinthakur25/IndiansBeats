
package com.tetravalstartups.dingdong.modules.discover;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DiscoverBanner {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<DiscoverBannerResponse> data = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public DiscoverBanner() {
    }

    /**
     * 
     * @param data
     * @param message
     * @param status
     */
    public DiscoverBanner(Boolean status, String message, List<DiscoverBannerResponse> data) {
        super();
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DiscoverBannerResponse> getData() {
        return data;
    }

    public void setData(List<DiscoverBannerResponse> data) {
        this.data = data;
    }

}
