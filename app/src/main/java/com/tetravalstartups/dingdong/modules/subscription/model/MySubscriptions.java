package com.tetravalstartups.dingdong.modules.subscription.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MySubscriptions {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<MySubscriptionResponse> mySubscriptionResponseList = null;

    /**
     * No args constructor for use in serialization
     */
    public MySubscriptions() {
    }

    /**
     * @param mySubscriptionResponseList
     * @param status
     */
    public MySubscriptions(Boolean status, List<MySubscriptionResponse> mySubscriptionResponseList) {
        super();
        this.status = status;
        this.mySubscriptionResponseList = mySubscriptionResponseList;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<MySubscriptionResponse> getData() {
        return mySubscriptionResponseList;
    }

    public void setData(List<MySubscriptionResponse> mySubscriptionResponseList) {
        this.mySubscriptionResponseList = mySubscriptionResponseList;
    }

}