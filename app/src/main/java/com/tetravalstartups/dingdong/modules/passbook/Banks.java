package com.tetravalstartups.dingdong.modules.passbook;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@IgnoreExtraProperties

public class Banks {
    @ServerTimestamp
    private Date timestamp;
    private String id;
    private String name;
    private String account;
    private String ifsc;
    private String branch;
    private String address;
    private int status;

    public Banks() {
    }

    public Banks(Date timestamp, String id, String name, String account, String ifsc, String branch, String address, int status) {
        this.timestamp = timestamp;
        this.id = id;
        this.name = name;
        this.account = account;
        this.ifsc = ifsc;
        this.branch = branch;
        this.address = address;
        this.status = status;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
