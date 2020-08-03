package com.tetravalstartups.dingdong.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.tetravalstartups.dingdong.MainActivity;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.utils.Constant;
import com.tetravalstartups.dingdong.utils.ProfilePhotoBottomSheet;

import java.util.HashMap;

public class SetupProfileActivity extends AppCompatActivity implements ProfilePhotoBottomSheet.BottomSheetListener, View.OnClickListener {

    private ImageView ivPhoto;
    private EditText etName;
    private EditText etEmail;
    private EditText etHandle;
    private EditText etBio;
    private TextView tvUpdate;
    private ImageView ivGoBack;

    private String selected_photo;

    private Master master;

    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;

    private ProfilePhotoBottomSheet bottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);
        initView();
    }

    private void initView() {
        ivPhoto = findViewById(R.id.ivPhoto);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etHandle = findViewById(R.id.etHandle);
        etBio = findViewById(R.id.etBio);
        ivGoBack = findViewById(R.id.ivGoBack);
        ivGoBack.setOnClickListener(this);

        tvUpdate = findViewById(R.id.tvUpdate);

        ivPhoto.setOnClickListener(this);
        tvUpdate.setOnClickListener(this);

        bottomSheet = new ProfilePhotoBottomSheet();

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        etHandle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String handle = etHandle.getText().toString();
                Query query = db.collection("users");
                query.whereEqualTo("handle", handle)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.getResult().getDocuments().isEmpty()) {
                                    etHandle.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_dd_user_handle_available, 0);
                                    tvUpdate.setVisibility(View.VISIBLE);
                                } else
                                if (handle.isEmpty()) {
                                    tvUpdate.setVisibility(View.GONE);
                                    etHandle.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
                                } else  {
                                    etHandle.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_dd_user_handle_unavailable, 0);
                                    tvUpdate.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });
    }

    @Override
    public void onButtonClicked(String text) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onClick(View view) {
        if (view == ivGoBack) {
            onBackPressed();
        }

        if (view == ivPhoto) {
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE};
            Permissions.check(this/*context*/, permissions,
                    null/*rationale*/, null/*options*/,
                    new PermissionHandler() {
                        @Override
                        public void onGranted() {
                            bottomSheet.show(getSupportFragmentManager(), "profilePhotoBottomSheet");
                        }
                    });
        }

        if (view == tvUpdate) {
            String name = etName.getText().toString();
            String email = etEmail.getText().toString();
            String handle = etHandle.getText().toString();
            String bio = etBio.getText().toString();

            if (TextUtils.isEmpty(name)) {
                Toast.makeText(this, "Full Name required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Email required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(handle)) {
                Toast.makeText(this, "Handle required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(bio)) {
                Toast.makeText(this, "Bio required", Toast.LENGTH_SHORT).show();
                return;
            }

            setProfileData(name, email, handle, bio);

        }
    }

    private void setProfileData(String name, String email, String handle, String bio) {
        Profile profile = new Profile();
        profile.setId(firebaseAuth.getCurrentUser().getUid());
        profile.setName(name);
        profile.setEmail(email);
        profile.setPhoto(Constant.DD_IV_PLACEHOLDER);
        profile.setBio(bio);
        profile.setHandle(handle);
        profile.setLikes(Constant.INITIAL_LIKES);
        profile.setFollowers(Constant.INITIAL_FOLLOWER);
        profile.setFollowing(Constant.INITIAL_FOLLOWING);

        HashMap hashMap = new HashMap();
        hashMap.put("reserved", "0");
        hashMap.put("unreserved", "0");
        hashMap.put("cashback", "0");
        hashMap.put("video", "0");
        hashMap.put("subscription", "0");


        db.collection("users")
                .document(firebaseAuth.getCurrentUser().getUid())
                .set(profile)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            db.collection("users")
                                    .document(firebaseAuth.getCurrentUser().getUid())
                                    .collection("passbook")
                                    .document("balance")
                                    .set(hashMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Intent intent = new Intent(SetupProfileActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                        }
                                    });
                        }
                    }
                });
    }

    public void closeSheet(){
        bottomSheet.dismiss();
    }

}