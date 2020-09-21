package com.tetravalstartups.dingdong.api;

import com.tetravalstartups.dingdong.modules.common.hashtag.model.TaggedVideos;
import com.tetravalstartups.dingdong.modules.discover.DiscoverBanner;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface CommonInterface {

    @GET("Common/BannerList")
    Call<DiscoverBanner> bannerFetch();

    @FormUrlEncoded
    @POST("Common/SearchByTag")
    Call<TaggedVideos> fetchTaggedVideos(@Field("requested_user_id") String requested_user_id, @Field("tag") String tag);

//    @FormUrlEncoded
//    @POST("Common/SearchDataByKeywords")
//    Call<SuperSearch> fetchDataByKeyword();

}
