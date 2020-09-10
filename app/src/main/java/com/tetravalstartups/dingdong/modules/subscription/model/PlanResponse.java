package com.tetravalstartups.dingdong.modules.subscription.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlanResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("benefit")
    @Expose
    private Integer benefit;
    @SerializedName("benefit_unit")
    @Expose
    private String benefitUnit;
    @SerializedName("total_benefit")
    @Expose
    private Integer totalBenefit;
    @SerializedName("upload_unit")
    @Expose
    private String uploadUnit;
    @SerializedName("uploads")
    @Expose
    private Integer uploads;
    @SerializedName("validity")
    @Expose
    private Integer validity;
    @SerializedName("validity_unit")
    @Expose
    private String validityUnit;

    /**
     * No args constructor for use in serialization
     */
    public PlanResponse() {
    }

    /**
     * @param amount
     * @param uploadUnit
     * @param name
     * @param validityUnit
     * @param id
     * @param validity
     * @param benefitUnit
     * @param totalBenefit
     * @param benefit
     * @param uploads
     */
    public PlanResponse(Integer id, String name, Integer amount, Integer benefit, String benefitUnit, Integer totalBenefit, String uploadUnit, Integer uploads, Integer validity, String validityUnit) {
        super();
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.benefit = benefit;
        this.benefitUnit = benefitUnit;
        this.totalBenefit = totalBenefit;
        this.uploadUnit = uploadUnit;
        this.uploads = uploads;
        this.validity = validity;
        this.validityUnit = validityUnit;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getBenefit() {
        return benefit;
    }

    public void setBenefit(Integer benefit) {
        this.benefit = benefit;
    }

    public String getBenefitUnit() {
        return benefitUnit;
    }

    public void setBenefitUnit(String benefitUnit) {
        this.benefitUnit = benefitUnit;
    }

    public Integer getTotalBenefit() {
        return totalBenefit;
    }

    public void setTotalBenefit(Integer totalBenefit) {
        this.totalBenefit = totalBenefit;
    }

    public String getUploadUnit() {
        return uploadUnit;
    }

    public void setUploadUnit(String uploadUnit) {
        this.uploadUnit = uploadUnit;
    }

    public Integer getUploads() {
        return uploads;
    }

    public void setUploads(Integer uploads) {
        this.uploads = uploads;
    }

    public Integer getValidity() {
        return validity;
    }

    public void setValidity(Integer validity) {
        this.validity = validity;
    }

    public String getValidityUnit() {
        return validityUnit;
    }

    public void setValidityUnit(String validityUnit) {
        this.validityUnit = validityUnit;
    }

}