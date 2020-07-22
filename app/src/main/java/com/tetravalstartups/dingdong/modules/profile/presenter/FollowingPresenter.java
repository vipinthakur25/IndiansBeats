package com.tetravalstartups.dingdong.modules.profile.presenter;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.tetravalstartups.dingdong.modules.profile.model.Followings;

import java.util.ArrayList;
import java.util.List;

public class FollowingPresenter {
    Context context;
    IFollowing iFollowing;
    private FirebaseFirestore db;

    public FollowingPresenter(Context context, IFollowing iFollowing) {
        this.context = context;
        this.iFollowing = iFollowing;
    }

    public interface IFollowing {
        void followingFetchSuccess(List<Followings> followingsList);

        void followingFetchError(String error);
    }

    public void fetchFollowing(String user_id) {
        db = FirebaseFirestore.getInstance();
        List<Followings> followingsList = new ArrayList<>();

        db.collection("users")
                .document(user_id)
                .collection("following")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots.getDocuments().isEmpty()) {
                            iFollowing.followingFetchError("No Followers");
                        } else {
                            followingsList.clear();
                            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                                Followings followings = snapshot.toObject(Followings.class);
                                followingsList.add(followings);
                            }

                            iFollowing.followingFetchSuccess(followingsList);

                        }
                    }
                });
    }

}
