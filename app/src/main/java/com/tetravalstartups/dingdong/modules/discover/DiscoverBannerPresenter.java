package com.tetravalstartups.dingdong.modules.discover;

import android.content.Context;

import com.tetravalstartups.dingdong.api.APIClient;
import com.tetravalstartups.dingdong.api.CommonInterface;
import com.tetravalstartups.dingdong.modules.profile.model.MyComplain;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiscoverBannerPresenter {
    private Context context;
    private IDiscoverBanner iDiscoverBanner;

    public DiscoverBannerPresenter(Context context, IDiscoverBanner iDiscoverBanner) {
        this.context = context;
        this.iDiscoverBanner = iDiscoverBanner;
    }

    public interface IDiscoverBanner{
        void fetchBannerResponse(DiscoverBanner discoverBanner);

        void fetchError(String error);
    }

public void fetchBanner(){
    CommonInterface commonInterface = APIClient.getRetrofitInstance().create(CommonInterface.class);
    Call<DiscoverBanner> discoverBannerCall = commonInterface.bannerFetch();

    discoverBannerCall.enqueue(new Callback<DiscoverBanner>() {
        @Override
        public void onResponse(Call<DiscoverBanner> call, Response<DiscoverBanner> response) {
            if (response.code() == 200){
            iDiscoverBanner.fetchBannerResponse(response.body());
            } else if (response.code() == 400) {
                iDiscoverBanner.fetchError(response.message());
            } else {
                iDiscoverBanner.fetchError("Something went wrong...");
            }
        }

        @Override
        public void onFailure(Call<DiscoverBanner> call, Throwable t) {
            iDiscoverBanner.fetchError(t.getMessage());
        }
    });
}
}
