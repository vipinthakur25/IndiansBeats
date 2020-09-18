package com.tetravalstartups.dingdong.api;

import com.tetravalstartups.dingdong.modules.passbook.model.GeneratePassbook;
import com.tetravalstartups.dingdong.modules.profile.model.ChatWithUs;
import com.tetravalstartups.dingdong.modules.profile.model.CheckHandle;
import com.tetravalstartups.dingdong.modules.profile.model.HelpRequest;
import com.tetravalstartups.dingdong.modules.profile.model.MyComplain;
import com.tetravalstartups.dingdong.modules.profile.model.ReportAProblem;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AuthInterface {
    @FormUrlEncoded
    @POST("Auth/PassbookCreate")
    Call<GeneratePassbook> generatePassbook(@Field("id") String id, @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("Auth/GetHelp")
    Call<ChatWithUs> chatWithUs(@Field("user_id") String id, @Field("description") String description, @Field("alternate_phone") String alternate_phone,
                                @Field("alternate_email") String alternate_email);

    @FormUrlEncoded
    @POST("Auth/ReportProblem")
    Call<ReportAProblem> reportProblem(@Field("user_id") String id, @Field("description") String description, @Field("image1") String image1,
                                       @Field("image2") String image2,
                                       @Field("image3") String image3);

    @FormUrlEncoded
    @POST("Auth/MyHelpRequestList")
    Call<HelpRequest> helpRequest(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("Auth/MyComplainsList")
    Call<MyComplain> complainRequest(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("Auth/CheckHandleAvaliblity")
    Call<CheckHandle> checkHandle(@Field("user_id") String user_id, @Field("handle") String handle);


}
