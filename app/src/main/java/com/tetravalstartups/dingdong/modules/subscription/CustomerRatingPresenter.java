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

public class CustomerRatingPresenter {
    Context context;
    IRating iRating;

    FirebaseFirestore db;

    public CustomerRatingPresenter(Context context, IRating iRating) {
        this.context = context;
        this.iRating = iRating;
    }

    public interface IRating {
        void fetchRatingSuccess(List<CustomerRating> customerRatingList);

        void fetchRatingError(String error);
    }

    public void fetchRating(){
        List<CustomerRating> customerRatingList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        db.collection("ratings")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (documentSnapshots.getDocuments().isEmpty()){
                            iRating.fetchRatingError("No Ratings");
                        } else {
                            customerRatingList.clear();
                            for (DocumentSnapshot snapshot : documentSnapshots.getDocuments()){
                                CustomerRating customerRating = snapshot.toObject(CustomerRating.class);
                                customerRatingList.add(customerRating);
                            }

                            iRating.fetchRatingSuccess(customerRatingList);

                        }
                    }
                });

    }

}
