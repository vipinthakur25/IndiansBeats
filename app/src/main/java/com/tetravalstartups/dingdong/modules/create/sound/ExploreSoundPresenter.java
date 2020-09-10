package com.tetravalstartups.dingdong.modules.create.sound;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExploreSoundPresenter {
    Context context;
    IExploreMusic iExploreMusic;

    private FirebaseFirestore db;

    public ExploreSoundPresenter(Context context, IExploreMusic iExploreMusic) {
        this.context = context;
        this.iExploreMusic = iExploreMusic;
    }

    public interface IExploreMusic {

        void musicFetchSuccess(List<Sound> soundList);

        void musicFetchError(String error);

    }

    public void fetchExploreMusic(){
        db = FirebaseFirestore.getInstance();
        List<Sound> soundList = new ArrayList<>();

        db.collection("sounds")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (documentSnapshots.getDocuments().isEmpty()){
                          //  iExploreMusic.musicFetchError("No Sounds");

                        } else {
                            soundList.clear();
                            for (DocumentSnapshot snapshot : documentSnapshots.getDocuments()){
                                Sound sound = snapshot.toObject(Sound.class);
                                soundList.add(sound);
                            }

                            iExploreMusic.musicFetchSuccess(soundList);

                        }
                    }
                });

    }




}
