package com.tetravalstartups.dingdong.modules.passbook.transactions.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Txn {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<TxnResponse> data = null;

    /**
     * No args constructor for use in serialization
     */
    public Txn() {
    }

    /**
     * @param data
     * @param status
     */
    public Txn(Boolean status, List<TxnResponse> data) {
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

    public List<TxnResponse> getData() {
        return data;
    }

    public void setData(List<TxnResponse> data) {
        this.data = data;
    }

}