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

public class UnreservedCoinTxnPresenter {

    Context context;
    IUnreservedTxn iTxn;

    public UnreservedCoinTxnPresenter(Context context, IUnreservedTxn iTxn) {
        this.context = context;
        this.iTxn = iTxn;
    }

    public interface IUnreservedTxn {
        void fetchTxnSuccess(List<UnreservedCoinTxn> unreservedCoinTxnList);

        void fetchTxnError(String error);
    }

    public void fetchTxn(String uid){
        List<UnreservedCoinTxn> unreservedCoinTxnList = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(uid)
                .collection("unreserved_coins_txn")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (documentSnapshots.getDocuments().isEmpty()){
                            iTxn.fetchTxnError("No Transactions");
                        } else {
                            unreservedCoinTxnList.clear();
                            for (DocumentSnapshot snapshot : documentSnapshots.getDocuments()){
                                UnreservedCoinTxn unreservedCoinTxn = snapshot.toObject(UnreservedCoinTxn.class);
                                unreservedCoinTxnList.add(unreservedCoinTxn);
                            }
                            iTxn.fetchTxnSuccess(unreservedCoinTxnList);
                        }
                    }
                });
    }

}
