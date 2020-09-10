package com.tetravalstartups.dingdong.modules.subscription;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.tetravalstartups.dingdong.api.APIClient;
import com.tetravalstartups.dingdong.api.PlanInterface;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.subscription.model.MySubscriptions;
import com.tetravalstartups.dingdong.modules.subscription.model.PlanResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubPlanPresenter {
    Context context;
    ISubPlan iSubscribed;

    private static final String TAG = "SubPlanPresenter";

    public SubPlanPresenter(Context context, ISubPlan iSubscribed) {
        this.context = context;
        this.iSubscribed = iSubscribed;
    }

    public void fetchSubscribe() {
        Master master = new Master(context);
        PlanInterface planInterface = APIClient.getRetrofitInstance().create(PlanInterface.class);
        Call<MySubscriptions> call = planInterface.fetchAvailableTask(master.getId());
        call.enqueue(new Callback<MySubscriptions>() {
            @Override
            public void onResponse(Call<MySubscriptions> call, Response<MySubscriptions> response) {
                if (response.code() == 200) {
                    iSubscribed.subscribeFetchSuccess(response.body());
                } else {
                    iSubscribed.subscribeFetchError(response.message());
                }
            }

            @Override
            public void onFailure(Call<MySubscriptions> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage() );
            }
        });
    }

    public interface ISubPlan {
        void subscribeFetchSuccess(MySubscriptions mySubscriptions);

        void subscribeFetchError(String error);
    }

}
