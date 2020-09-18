package com.tetravalstartups.dingdong.api;

import com.tetravalstartups.dingdong.modules.discover.DiscoverBanner;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CommonInterface {

    @GET("Common/BannerList")
    Call<DiscoverBanner> bannerFetch();
}
