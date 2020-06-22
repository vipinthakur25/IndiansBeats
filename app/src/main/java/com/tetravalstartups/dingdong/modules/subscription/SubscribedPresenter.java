package com.tetravalstartups.dingdong.modules.subscription;

import android.content.Context;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SubscribedPresenter {
    Context context;
    ISubscribed iSubscribed;

    public SubscribedPresenter(Context context, ISubscribed iSubscribed) {
        this.context = context;
        this.iSubscribed = iSubscribed;
    }

    public interface ISubscribed {
        void subscribeFetchSuccess(List<Subscribed> subscribedList);

        void subscribeFetchError(String error);
    }

    public void fetchSubscribe(String uid){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<Subscribed> subscribedList = new ArrayList<>();

        db.collection("users")
                .document(uid)
                .collection("subscription")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (documentSnapshots.getDocuments().isEmpty()){
                            iSubscribed.subscribeFetchError("No Subscriptions");
                        } else {
                            subscribedList.clear();
                            for (DocumentSnapshot snapshot : documentSnapshots.getDocuments()){
                                Subscribed subscribed = snapshot.toObject(Subscribed.class);
                                subscribedList.add(subscribed);
                            }

                            iSubscribed.subscribeFetchSuccess(subscribedList);

                        }
                    }
                });

    }

}
