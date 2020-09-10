package com.tetravalstartups.dingdong.modules.passbook;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BanksPresenter {
    Context context;
    IBanks iBanks;
    FirebaseFirestore db;

    public BanksPresenter(Context context, IBanks iBanks) {
        this.context = context;
        this.iBanks = iBanks;
    }

    public interface IBanks {
        void fetchBanksSuccess(List<Banks> banksList);

        void fetchBanksError(String error);
    }

    public void fetchBanks(String uid){
        db = FirebaseFirestore.getInstance();
        List<Banks> banksList = new ArrayList<>();

//        DocumentReference documentReference = db.collection("customers").document();
//        String id = documentReference.getId();

        db.collection("customers")
                .document(uid)
                .collection("banks")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (documentSnapshots.getDocuments().isEmpty()){
                            iBanks.fetchBanksError("No banks");
                        } else {
                            banksList.clear();
                            for (DocumentSnapshot snapshot : documentSnapshots.getDocuments()){
                                Banks banks = snapshot.toObject(Banks.class);
                                banksList.add(banks);
                            }

                            iBanks.fetchBanksSuccess(banksList);

                        }
                    }
                });

    }

}
