package com.tetravalstartups.dingdong.modules.subscription;

import android.content.Context;
import android.util.Log;

import com.tetravalstartups.dingdong.api.APIClient;
import com.tetravalstartups.dingdong.api.PlanInterface;
import com.tetravalstartups.dingdong.modules.subscription.model.Plans;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubscriptionPresenter {

    private static final String TAG = "SubscriptionPresenter";
    Context context;
    ISubs iSubs;

    public SubscriptionPresenter(Context context, ISubs iSubs) {
        this.context = context;
        this.iSubs = iSubs;
    }

    public void fetchSubs() {
        PlanInterface planInterface = APIClient.getRetrofitInstance().create(PlanInterface.class);
        Call<Plans> call = planInterface.fetchPlans();
        call.enqueue(new Callback<Plans>() {
            @Override
            public void onResponse(Call<Plans> call, Response<Plans> response) {
                if (response.code() == 200) {
                    iSubs.subsFetchSuccess(response.body());
                } else {
                    iSubs.subsFetchError(response.message());
                }
            }

            @Override
            public void onFailure(Call<Plans> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                iSubs.subsFetchError(t.getMessage());
            }
        });

    }

    public interface ISubs {
        void subsFetchSuccess(Plans plans);

        void subsFetchError(String error);
    }

}
