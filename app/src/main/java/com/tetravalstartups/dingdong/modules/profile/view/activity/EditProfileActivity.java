package com.tetravalstartups.dingdong.modules.profile.view.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.tetravalstartups.dingdong.MainActivity;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.auth.Profile;
import com.tetravalstartups.dingdong.utils.LightBox;
import com.tetravalstartups.dingdong.utils.ProfilePhotoBottomSheet;

public class EditProfileActivity extends AppCompatActivity implements ProfilePhotoBottomSheet.BottomSheetListener, View.OnClickListener {

    private ImageView ivPhoto;
    private EditText etName;
    private EditText etHandle;
    private EditText etBio;
    private TextView tvUpdate;

    private String selected_photo;

    private Master master;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initView();
    }

    private void initView() {
        ivPhoto = findViewById(R.id.ivPhoto);
        etName = findViewById(R.id.etName);
        etHandle = findViewById(R.id.etHandle);
        etBio = findViewById(R.id.etBio);

        tvUpdate = findViewById(R.id.tvUpdate);

        ivPhoto.setOnClickListener(this);
        tvUpdate.setOnClickListener(this);

        getProfileData();

    }

    private void setupProfile() {
        etName.setText(master.getName());
        etHandle.setText(master.getHandle());
        if (!master.getBio().equals("Add bio")) {
            etBio.setText(master.getBio());
        }

        Glide.with(EditProfileActivity.this)
                .load(master.getPhoto())
                .placeholder(R.drawable.dingdong_placeholder)
                .into(ivPhoto);

    }

    @Override
    public void onButtonClicked(String text) {

    }

    @Override
    public void onClick(View v) {
        if (v == ivPhoto){
            ProfilePhotoBottomSheet bottomSheet = new ProfilePhotoBottomSheet();
            bottomSheet.show(getSupportFragmentManager(), "profilePhotoBottomSheet");
        }

        if (v == tvUpdate){

        }
    }

    public void getProfileData(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String id  = auth.getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(id)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        Profile profile = documentSnapshot.toObject(Profile.class);
                        new Master(EditProfileActivity.this).setUser(profile);
                        master = new Master(EditProfileActivity.this);
                        setupProfile();
                    }
                });
    }

}