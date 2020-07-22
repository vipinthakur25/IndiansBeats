package com.tetravalstartups.dingdong.modules.create.sound;

import android.content.Context;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.tetravalstartups.dingdong.auth.Master;

import java.util.ArrayList;
import java.util.List;

public class FavoriteSoundPresenter {
    Context context;
    IFav iFav;
    FirebaseFirestore db;
    Master master;

    public FavoriteSoundPresenter(Context context, IFav iFav) {
        this.context = context;
        this.iFav = iFav;
    }

    public interface IFav {
        void musicFetchSuccess(List<FavSound> favSoundList);

        void musicFetchError(String error);
    }

    public void fetchFavMusic(){
        master = new Master(context);
        db = FirebaseFirestore.getInstance();
        List<FavSound> soundList = new ArrayList<>();

        db.collection("users")
                .document(master.getId())
                .collection("sounds")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (documentSnapshots.getDocuments().isEmpty()){
                            iFav.musicFetchError("No Sounds");

                        } else {
                            soundList.clear();
                            for (DocumentSnapshot snapshot : documentSnapshots.getDocuments()){
                                FavSound sound = snapshot.toObject(FavSound.class);
                                soundList.add(sound);
                            }

                            iFav.musicFetchSuccess(soundList);

                        }
                    }
                });

    }
}
