package com.tetravalstartups.dingdong.api;

import com.tetravalstartups.dingdong.modules.passbook.model.GeneratePassbook;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AuthInterface {
    @FormUrlEncoded
    @POST("Auth/PassbookCreate")
    Call<GeneratePassbook> generatePassbook(@Field("id") String id, @Field("user_id") String user_id);
}
