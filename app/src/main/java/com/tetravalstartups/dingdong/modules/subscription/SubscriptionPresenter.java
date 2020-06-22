package com.tetravalstartups.dingdong.modules.subscription;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionPresenter {

    Context context;
    ISubs iSubs;

    public SubscriptionPresenter(Context context, ISubs iSubs) {
        this.context = context;
        this.iSubs = iSubs;
    }

    public interface ISubs {
        void subsFetchSuccess(List<Subscription> subscriptionList);

        void subsFetchError(String error);
    }

    public void fetchSubs(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<Subscription> subscriptionList = new ArrayList<>();

        Query query = db.collection("subscription");
        query.orderBy("id", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            if (task.getResult().getDocuments().isEmpty()){
                                iSubs.subsFetchError("No subscription plans");
                            } else {
                                for (DocumentSnapshot snapshot : task.getResult()){
                                    Subscription subscription = snapshot.toObject(Subscription.class);
                                    subscriptionList.add(subscription);
                                }

                                iSubs.subsFetchSuccess(subscriptionList);

                            }
                        } else {
                            iSubs.subsFetchError(task.getException().getMessage());
                        }
                    }
                });


    }

}
