package com.tetravalstartups.dingdong.modules.passbook;

import android.content.Context;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ReservedCoinTxnPresenter {

    Context context;
    ITxn iTxn;

    public ReservedCoinTxnPresenter(Context context, ITxn iTxn) {
        this.context = context;
        this.iTxn = iTxn;
    }

    public interface ITxn {
        void fetchTxnSuccess(List<ReservedCoinsTxn> reservedCoinsTxnList);

        void fetchTxnError(String error);
    }

    public void fetchTxn(String uid){
        List<ReservedCoinsTxn> reservedCoinsTxnList = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(uid)
                .collection("reserved_coins_txn")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (documentSnapshots.getDocuments().isEmpty()){
                            iTxn.fetchTxnError("No Transactions");
                        } else {
                            reservedCoinsTxnList.clear();
                            for (DocumentSnapshot snapshot : documentSnapshots.getDocuments()){
                                ReservedCoinsTxn reservedCoinsTxn = snapshot.toObject(ReservedCoinsTxn.class);
                                reservedCoinsTxnList.add(reservedCoinsTxn);
                            }
                            iTxn.fetchTxnSuccess(reservedCoinsTxnList);
                        }
                    }
                });
    }

}
