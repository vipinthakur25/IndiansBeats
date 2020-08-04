package com.tetravalstartups.dingdong.modules.passbook;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@IgnoreExtraProperties
public class PayoutRequest {

    @ServerTimestamp
    private Date timestamp;
    private String id;
    private String user_id;
    private String user_name;
    private String user_handle;
    private String user_email;
    private String user_photo;
    private String request_type;
    private String coin_balance_at_request;
    private String inr_balance_at_request;
    private String conversion_rate_at_request;
    private String redeemable_percent_at_request;
    private String redeemable_balance_at_request;
    private String processing_fee_percent_at_request;
    private String processing_fee_amount_at_request;
    private String in_hand_balance_at_request;
    private String to_withdraw_balance_at_request;
    private String txn_date;
    private String txn_time;
    private String status;

    public PayoutRequest() {
    }

    public PayoutRequest(Date timestamp, String id, String user_id, String user_name, String user_handle, String user_email, String user_photo, String request_type, String coin_balance_at_request, String inr_balance_at_request, String conversion_rate_at_request, String redeemable_percent_at_request, String redeemable_balance_at_request, String processing_fee_percent_at_request, String processing_fee_amount_at_request, String in_hand_balance_at_request, String to_withdraw_balance_at_request, String txn_date, String txn_time, String status) {
        this.timestamp = timestamp;
        this.id = id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_handle = user_handle;
        this.user_email = user_email;
        this.user_photo = user_photo;
        this.request_type = request_type;
        this.coin_balance_at_request = coin_balance_at_request;
        this.inr_balance_at_request = inr_balance_at_request;
        this.conversion_rate_at_request = conversion_rate_at_request;
        this.redeemable_percent_at_request = redeemable_percent_at_request;
        this.redeemable_balance_at_request = redeemable_balance_at_request;
        this.processing_fee_percent_at_request = processing_fee_percent_at_request;
        this.processing_fee_amount_at_request = processing_fee_amount_at_request;
        this.in_hand_balance_at_request = in_hand_balance_at_request;
        this.to_withdraw_balance_at_request = to_withdraw_balance_at_request;
        this.txn_date = txn_date;
        this.txn_time = txn_time;
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_handle() {
        return user_handle;
    }

    public void setUser_handle(String user_handle) {
        this.user_handle = user_handle;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }

    public String getCoin_balance_at_request() {
        return coin_balance_at_request;
    }

    public void setCoin_balance_at_request(String coin_balance_at_request) {
        this.coin_balance_at_request = coin_balance_at_request;
    }

    public String getInr_balance_at_request() {
        return inr_balance_at_request;
    }

    public void setInr_balance_at_request(String inr_balance_at_request) {
        this.inr_balance_at_request = inr_balance_at_request;
    }

    public String getConversion_rate_at_request() {
        return conversion_rate_at_request;
    }

    public void setConversion_rate_at_request(String conversion_rate_at_request) {
        this.conversion_rate_at_request = conversion_rate_at_request;
    }

    public String getRedeemable_percent_at_request() {
        return redeemable_percent_at_request;
    }

    public void setRedeemable_percent_at_request(String redeemable_percent_at_request) {
        this.redeemable_percent_at_request = redeemable_percent_at_request;
    }

    public String getRedeemable_balance_at_request() {
        return redeemable_balance_at_request;
    }

    public void setRedeemable_balance_at_request(String redeemable_balance_at_request) {
        this.redeemable_balance_at_request = redeemable_balance_at_request;
    }

    public String getProcessing_fee_percent_at_request() {
        return processing_fee_percent_at_request;
    }

    public void setProcessing_fee_percent_at_request(String processing_fee_percent_at_request) {
        this.processing_fee_percent_at_request = processing_fee_percent_at_request;
    }

    public String getProcessing_fee_amount_at_request() {
        return processing_fee_amount_at_request;
    }

    public void setProcessing_fee_amount_at_request(String processing_fee_amount_at_request) {
        this.processing_fee_amount_at_request = processing_fee_amount_at_request;
    }

    public String getIn_hand_balance_at_request() {
        return in_hand_balance_at_request;
    }

    public void setIn_hand_balance_at_request(String in_hand_balance_at_request) {
        this.in_hand_balance_at_request = in_hand_balance_at_request;
    }

    public String getTo_withdraw_balance_at_request() {
        return to_withdraw_balance_at_request;
    }

    public void setTo_withdraw_balance_at_request(String to_withdraw_balance_at_request) {
        this.to_withdraw_balance_at_request = to_withdraw_balance_at_request;
    }

    public String getTxn_date() {
        return txn_date;
    }

    public void setTxn_date(String txn_date) {
        this.txn_date = txn_date;
    }

    public String getTxn_time() {
        return txn_time;
    }

    public void setTxn_time(String txn_time) {
        this.txn_time = txn_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
