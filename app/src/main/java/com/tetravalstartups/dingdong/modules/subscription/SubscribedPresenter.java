package com.tetravalstartups.dingdong.modules.subscription;

import android.content.Context;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.tetravalstartups.dingdong.api.APIClient;
import com.tetravalstartups.dingdong.api.PlanInterface;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.subscription.model.MySubscriptionResponse;
import com.tetravalstartups.dingdong.modules.subscription.model.MySubscriptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubscribedPresenter {
    Context context;
    ISubscribed iSubscribed;

    private static final String TAG = "SubscribedPresenter";

    public SubscribedPresenter(Context context, ISubscribed iSubscribed) {
        this.context = context;
        this.iSubscribed = iSubscribed;
    }

    public interface ISubscribed {
        void subscribeFetchSuccess(MySubscriptions mySubscriptions);

        void subscribeFetchError(String error);
    }

    public void fetchSubscribe(){
        Master master = new Master(context);
        PlanInterface planInterface = APIClient.getRetrofitInstance().create(PlanInterface.class);
        Call<MySubscriptions> call = planInterface.fetchMySubs(master.getId());
        call.enqueue(new Callback<MySubscriptions>() {
            @Override
            public void onResponse(Call<MySubscriptions> call, Response<MySubscriptions> response) {
                if (response.code() == 200) {
                    iSubscribed.subscribeFetchSuccess(response.body());
                } else if (response.code() == 400) {
                    iSubscribed.subscribeFetchError("No Subscriptions");
                }
            }

            @Override
            public void onFailure(Call<MySubscriptions> call, Throwable t) {
                iSubscribed.subscribeFetchError(t.getMessage());
            }
        });
    }

}
