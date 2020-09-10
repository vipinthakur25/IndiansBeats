package com.tetravalstartups.dingdong.api;

import com.tetravalstartups.dingdong.modules.passbook.model.BanksResponse;
import com.tetravalstartups.dingdong.modules.passbook.model.UpdatePassbook;
import com.tetravalstartups.dingdong.modules.passbook.redeem.model.PassbookBalance;
import com.tetravalstartups.dingdong.modules.passbook.redeem.model.PayoutHistory;
import com.tetravalstartups.dingdong.modules.passbook.redeem.model.PayoutRequestResponse;
import com.tetravalstartups.dingdong.modules.passbook.transactions.model.Txn;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserInterface {

    @FormUrlEncoded
    @POST("User/usertransaction")
    Call<Txn> fetchTxn(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("User/UpdatePassbookData")
    Call<UpdatePassbook> updatePassbook(@Field("user_id") String user_id,
                                        @Field("daily_rewards") String daily_rewards,
                                        @Field("fans_donation") String fans_donation,
                                        @Field("subscription") String subscription,
                                        @Field("time_spent") String time_spent,
                                        @Field("video_uploads") String video_uploads);

    @FormUrlEncoded
    @POST("User/PassbookDetails")
    Call<PassbookBalance> fetchPassbookDetails(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("User/PayoutRequest")
    Call<PayoutRequestResponse> payoutRequest(@Field("id") String id,
                                              @Field("user_id") String user_id,
                                              @Field("amount") String amount,
                                              @Field("type") String type);

    @FormUrlEncoded
    @POST("User/UserPayoutHistory")
    Call<PayoutHistory> fetchPayoutHistory(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("User/AddUpdateUserBankDetails")
    Call<BanksResponse> updateBank(@Field("id") String id,
                                   @Field("user_id") String user_id,
                                   @Field("account") String account,
                                   @Field("address") String address,
                                   @Field("branch") String branch,
                                   @Field("ifsc") String ifsc,
                                   @Field("status") String status);
}
