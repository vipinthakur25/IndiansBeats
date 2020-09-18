package com.tetravalstartups.dingdong.modules.profile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HelpRequestResponse {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("alternate_phone")
    @Expose
    private String alternatePhone;
    @SerializedName("alternate_email")
    @Expose
    private String alternateEmail;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("request_date")
    @Expose
    private String requestDate;
    @SerializedName("status")
    @Expose
    private Integer status;

    /**
     * No args constructor for use in serialization
     */
    public HelpRequestResponse() {
    }

    /**
     * @param alternateEmail
     * @param alternatePhone
     * @param requestDate
     * @param description
     * @param id
     * @param userId
     * @param status
     */
    public HelpRequestResponse(String id, String alternatePhone, String alternateEmail, String description, String userId, String requestDate, Integer status) {
        super();
        this.id = id;
        this.alternatePhone = alternatePhone;
        this.alternateEmail = alternateEmail;
        this.description = description;
        this.userId = userId;
        this.requestDate = requestDate;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlternatePhone() {
        return alternatePhone;
    }

    public void setAlternatePhone(String alternatePhone) {
        this.alternatePhone = alternatePhone;
    }

    public String getAlternateEmail() {
        return alternateEmail;
    }

    public void setAlternateEmail(String alternateEmail) {
        this.alternateEmail = alternateEmail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}