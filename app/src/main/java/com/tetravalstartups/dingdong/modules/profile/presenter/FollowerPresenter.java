package com.tetravalstartups.dingdong.modules.profile.presenter;

import android.content.Context;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.tetravalstartups.dingdong.modules.profile.model.Followers;
import com.tetravalstartups.dingdong.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class FollowerPresenter {
    Context context;
    IFollower iFollower;

    FirebaseFirestore db;

    public FollowerPresenter(Context context, IFollower iFollower) {
        this.context = context;
        this.iFollower = iFollower;
    }

    public interface IFollower {
        void followerFetchSuccess(List<Followers> followersList);

        void followerFetchError(String error);
    }

    public void fetchFollowers(){
        db = FirebaseFirestore.getInstance();
        final List<Followers> followersList = new ArrayList<>();
        db.collection("users")
                .document(Constants.SAMPLE_USER_ID)
                .collection("followers")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (documentSnapshots.getDocuments().isEmpty()){
                            iFollower.followerFetchError("No Followers...");
                        } else {
                            followersList.clear();
                            for (DocumentSnapshot snapshot : documentSnapshots.getDocuments()){
                                Followers followers = snapshot.toObject(Followers.class);
                                followersList.add(followers);
                            }
                            iFollower.followerFetchSuccess(followersList);
                        }
                    }
                });

    }

}
