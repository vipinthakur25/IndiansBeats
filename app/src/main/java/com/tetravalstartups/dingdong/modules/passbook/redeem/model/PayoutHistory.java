package com.tetravalstartups.dingdong.modules.passbook.redeem.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PayoutHistory {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<PayoutHistoryResponse> data = null;

    /**
     * No args constructor for use in serialization
     */
    public PayoutHistory() {
    }

    /**
     * @param data
     * @param status
     */
    public PayoutHistory(Boolean status, List<PayoutHistoryResponse> data) {
        super();
        this.status = status;
        this.data = data;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<PayoutHistoryResponse> getData() {
        return data;
    }

    public void setData(List<PayoutHistoryResponse> data) {
        this.data = data;
    }

}