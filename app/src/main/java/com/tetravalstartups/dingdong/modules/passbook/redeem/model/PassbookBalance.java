package com.tetravalstartups.dingdong.modules.passbook.redeem.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PassbookBalance {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private PassbookBalanceResponse data;

    /**
     * No args constructor for use in serialization
     */
    public PassbookBalance() {
    }

    /**
     * @param data
     * @param status
     */
    public PassbookBalance(Boolean status, PassbookBalanceResponse data) {
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

    public PassbookBalanceResponse getData() {
        return data;
    }

    public void setData(PassbookBalanceResponse data) {
        this.data = data;
    }

}