package com.tetravalstartups.dingdong.modules.profile.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.profile.model.Avatar;
import com.tetravalstartups.dingdong.modules.profile.view.adapter.AvatarAdapter;
import com.tetravalstartups.dingdong.utils.EqualSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class AvatarPhotoFragment extends Fragment {

    private View view;
    private RecyclerView recyclerAvatar;

    public AvatarPhotoFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_avatar_photo, container, false);
        initView();
        return view;
    }

    private void initView() {
        recyclerAvatar = view.findViewById(R.id.recyclerAvatar);
        fetchAvatar();
    }

    private void fetchAvatar() {
        recyclerAvatar.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerAvatar.addItemDecoration(new EqualSpacingItemDecoration(16, EqualSpacingItemDecoration.GRID));
        final List<Avatar> avatarList = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("avatars")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            avatarList.clear();
                            for (DocumentSnapshot snapshot : task.getResult()){
                                Avatar avatar = snapshot.toObject(Avatar.class);
                                avatarList.add(avatar);
                            }
                            AvatarAdapter avatarAdapter = new AvatarAdapter(getContext(), avatarList);
                            avatarAdapter.notifyDataSetChanged();
                            recyclerAvatar.setAdapter(avatarAdapter);
                        }
                    }
                });
    }
}
