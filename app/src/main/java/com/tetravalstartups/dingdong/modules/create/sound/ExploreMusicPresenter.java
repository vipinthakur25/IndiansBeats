package com.tetravalstartups.dingdong.modules.create.sound;

import android.content.Context;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ExploreMusicPresenter {
    Context context;
    IExploreMusic iExploreMusic;

    private FirebaseFirestore db;

    public ExploreMusicPresenter(Context context, IExploreMusic iExploreMusic) {
        this.context = context;
        this.iExploreMusic = iExploreMusic;
    }

    public interface IExploreMusic {

        void musicFetchSuccess(List<Music> musicList);

        void musicFetchError(String error);

    }

    public void fetchExploreMusic(){
        db = FirebaseFirestore.getInstance();
        List<Music> musicList = new ArrayList<>();

        db.collection("sounds")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (documentSnapshots.getDocuments().isEmpty()){

                            iExploreMusic.musicFetchError("No Sounds");

                        } else {
                            musicList.clear();
                            for (DocumentSnapshot snapshot : documentSnapshots.getDocuments()){
                                Music music = snapshot.toObject(Music.class);
                                musicList.add(music);
                            }

                            iExploreMusic.musicFetchSuccess(musicList);

                        }
                    }
                });

    }



}
