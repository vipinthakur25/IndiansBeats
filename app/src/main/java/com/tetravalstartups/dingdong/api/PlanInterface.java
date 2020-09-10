package com.tetravalstartups.dingdong.api;

import com.tetravalstartups.dingdong.modules.passbook.transactions.model.TransactionResponse;
import com.tetravalstartups.dingdong.modules.publish.AvailableTask;
import com.tetravalstartups.dingdong.modules.publish.UseTask;
import com.tetravalstartups.dingdong.modules.subscription.model.MySubscriptions;
import com.tetravalstartups.dingdong.modules.subscription.model.PaymentKey;
import com.tetravalstartups.dingdong.modules.subscription.model.Plans;
import com.tetravalstartups.dingdong.modules.subscription.model.SubscriptionPurchaseResponse;
import com.tetravalstartups.dingdong.modules.subscription.model.SubscriptionResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PlanInterface {

    @FormUrlEncoded
    @POST("Plan/AddTransection")
    Call<TransactionResponse> performTransaction(@Field("id") String id,
                                                 @Field("user_id") String user_id,
                                                 @Field("amount") String amount,
                                                 @Field("remark") String remark,
                                                 @Field("date") String date,
                                                 @Field("time") String time,
                                                 @Field("status") int status);

    @FormUrlEncoded
    @POST("Plan/ActivePlan")
    Call<SubscriptionPurchaseResponse> purchaseSubscription(@Field("id") String id,
                                                            @Field("user_id") String user_id,
                                                            @Field("plan_id") int plan_id,
                                                            @Field("transection_id") String transection_id);

    @FormUrlEncoded
    @POST("Plan/addplansubscription")
    Call<SubscriptionResponse> addSubs(@Field("id") String id,
                                       @Field("name") String name,
                                       @Field("total_uploads") String total_uploads,
                                       @Field("avl_uploads") String avl_uploads,
                                       @Field("start_date") String start_date,
                                       @Field("end_date") String end_date,
                                       @Field("monthly_profit") String monthly_profit,
                                       @Field("status") String status,
                                       @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("Plan/MySubscription")
    Call<MySubscriptions> fetchMySubs(@Field("user_id") String user_id);

    @GET("Plan/List")
    Call<Plans> fetchPlans();

    @GET("Plan/GetPaymentKey")
    Call<PaymentKey> fetchPaymentKey();

    @FormUrlEncoded
    @POST("Plan/UserActiveplans")
    Call<MySubscriptions> fetchAvailableTask(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("Plan/UpdateVideoCount")
    Call<UseTask> doUseTask(@Field("type") int type,
                            @Field("user_id") String user_id,
                            @Field("subscription_id") String subscription_id,
                            @Field("earn_amount") int earn_amount);

}
