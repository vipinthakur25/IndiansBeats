package com.tetravalstartups.dingdong;

import android.content.Context;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.tetravalstartups.dingdong.utils.Constants;

public class BalancePresenter {
    Context context;
    IBalance iBalance;

    public BalancePresenter(Context context, IBalance iBalance) {
        this.context = context;
        this.iBalance = iBalance;
    }

    public interface IBalance {
        void balanceFetchSuccess(int balance);
        void balanceFetchFail(String error);
    }

    public void fetchCoins(String uid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.USER_REFERENCE)
                .document(uid)
                .collection(Constants.COIN_REFERENCE)
                .document("balances")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        String cb = documentSnapshot.get("coin_balance").toString();
                        int balance = Integer.parseInt(cb);
                    }
                });
    }

}
