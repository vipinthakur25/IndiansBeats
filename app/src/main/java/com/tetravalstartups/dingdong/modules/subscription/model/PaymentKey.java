package com.tetravalstartups.dingdong.modules.subscription.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentKey {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("Key")
    @Expose
    private String key;
    @SerializedName("upi")
    @Expose
    private String upi;

    /**
     * No args constructor for use in serialization
     */
    public PaymentKey() {
    }

    /**
     * @param key
     * @param status
     * @param upi
     */
    public PaymentKey(Boolean status, String key, String upi) {
        super();
        this.status = status;
        this.key = key;
        this.upi = upi;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUpi() {
        return upi;
    }

    public void setUpi(String upi) {
        this.upi = upi;
    }

}