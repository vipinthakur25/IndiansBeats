package com.tetravalstartups.dingdong.modules.subscription;

import android.content.Context;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SubPlanPresenter {
    Context context;
    ISubPlan iSubscribed;

    public SubPlanPresenter(Context context, ISubPlan iSubscribed) {
        this.context = context;
        this.iSubscribed = iSubscribed;
    }

    public interface ISubPlan {
        void subscribeFetchSuccess(List<SubPlan> subPlanList);

        void subscribeFetchError(String error);
    }

    public void fetchSubscribe(String uid){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<SubPlan> subPlanList = new ArrayList<>();

        db.collection("users")
                .document(uid)
                .collection("subscription")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (documentSnapshots.getDocuments().isEmpty()){
                            iSubscribed.subscribeFetchError("No Subscriptions");
                        } else {
                            subPlanList.clear();
                            for (DocumentSnapshot snapshot : documentSnapshots.getDocuments()){
                                if (!snapshot.getString("avl_uploads").equals("0") || snapshot.getString("status").equals("1")) {
                                    SubPlan subPlan = snapshot.toObject(SubPlan.class);
                                    subPlanList.add(subPlan);
                                }
                            }

                            iSubscribed.subscribeFetchSuccess(subPlanList);

                        }
                    }
                });

    }

}
