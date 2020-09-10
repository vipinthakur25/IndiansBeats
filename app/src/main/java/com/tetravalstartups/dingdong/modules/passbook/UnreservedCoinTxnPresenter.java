package com.tetravalstartups.dingdong.modules.passbook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.tetravalstartups.dingdong.api.APIClient;
import com.tetravalstartups.dingdong.api.UserInterface;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.passbook.redeem.model.PayoutHistory;
import com.tetravalstartups.dingdong.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnreservedCoinTxnPresenter {

    Context context;
    IUnreservedTxn iTxn;
    private UserInterface userInterface;
    private Master master;

    private static final String TAG = "UnreservedCoinTxnPresen";

    public UnreservedCoinTxnPresenter(Context context, IUnreservedTxn iTxn) {
        this.context = context;
        this.iTxn = iTxn;
    }

    public interface IUnreservedTxn {
        void fetchTxnSuccess(PayoutHistory payoutHistory);

        void fetchTxnError(String error);
    }

    public void fetchTxn(){
        master = new Master(context);
        userInterface = APIClient.getRetrofitInstance().create(UserInterface.class);
        Call<PayoutHistory> call = userInterface.fetchPayoutHistory(master.getId());
        call.enqueue(new Callback<PayoutHistory>() {
            @Override
            public void onResponse(Call<PayoutHistory> call, Response<PayoutHistory> response) {
                if (response.code() == 200) {
                    iTxn.fetchTxnSuccess(response.body());
                } else {
                    iTxn.fetchTxnError(response.message());
                }
            }

            @Override
            public void onFailure(Call<PayoutHistory> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage());
            }
        });
    }

}
