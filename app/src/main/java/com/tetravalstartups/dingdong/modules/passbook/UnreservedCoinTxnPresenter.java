package com.tetravalstartups.dingdong.modules.passbook;

import android.content.Context;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.tetravalstartups.dingdong.utils.Constant;

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
        Query query = db.collection("payouts").whereEqualTo("user_id", uid);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots.getDocuments().isEmpty()) {
                    iTxn.fetchTxnError("No Data");
                } else {
                    unreservedCoinTxnList.clear();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                        UnreservedCoinTxn unreservedCoinTxn = new UnreservedCoinTxn();
                        unreservedCoinTxn.setId(snapshot.getString("id"));
                        unreservedCoinTxn.setAmount(snapshot.getString("to_withdraw_balance_at_request"));
                        unreservedCoinTxn.setRemark("Payout Request");
                        unreservedCoinTxn.setStatus("0");
                        unreservedCoinTxn.setDate(snapshot.getString("txn_date"));
                        unreservedCoinTxn.setTime(snapshot.getString("txn_time"));
                        unreservedCoinTxnList.add(unreservedCoinTxn);
                    }

                    iTxn.fetchTxnSuccess(unreservedCoinTxnList);

                }
            }
        });
    }

}
