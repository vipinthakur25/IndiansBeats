package com.tetravalstartups.dingdong.modules.passbook;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.tetravalstartups.dingdong.api.APIClient;
import com.tetravalstartups.dingdong.api.UserInterface;
import com.tetravalstartups.dingdong.modules.passbook.transactions.model.Txn;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionPresenter {
    private Context context;
    private ITransaction iTransaction;

    public TransactionPresenter(Context context, ITransaction iTransaction) {
        this.context = context;
        this.iTransaction = iTransaction;
    }

    public interface ITransaction {
        void fetchSuccess(Txn txn);

        void fetchError(String error);
    }

    public void fetchTxn(String id) {
        UserInterface userInterface = APIClient.getRetrofitInstance().create(UserInterface.class);
        Call<Txn> txnCall = userInterface.fetchTxn(id);
        txnCall.enqueue(new Callback<Txn>() {
            @Override
            public void onResponse(Call<Txn> call, Response<Txn> response) {
                if (response.code() == 200) {
                    iTransaction.fetchSuccess(response.body());
                } else {
                    iTransaction.fetchError(response.message());
                }
            }

            @Override
            public void onFailure(Call<Txn> call, Throwable t) {
                iTransaction.fetchError(t.getMessage());
            }
        });
    }

}
