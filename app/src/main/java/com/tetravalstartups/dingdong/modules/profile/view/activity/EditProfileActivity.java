package com.tetravalstartups.dingdong.modules.profile.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.tetravalstartups.dingdong.api.APIClient;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.api.RequestInterface;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.auth.Profile;
import com.tetravalstartups.dingdong.modules.profile.external.PublicProfile;
import com.tetravalstartups.dingdong.utils.ProfilePhotoBottomSheet;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity implements ProfilePhotoBottomSheet.BottomSheetListener, View.OnClickListener {

    private ImageView ivPhoto;
    private EditText etName;
    private EditText etHandle;
    private EditText etBio;
    private TextView tvUpdate;
    private ImageView ivGoBack;

    private String selected_photo;

    private Master master;

    private FirebaseFirestore db;

    private ProfilePhotoBottomSheet bottomSheet;
    private RequestInterface requestInterface;
    private Profile userProfile;
    private static final String TAG = "EditProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        db = FirebaseFirestore.getInstance();
        master = new Master(EditProfileActivity.this);

        initView();
    }

    private void initView() {
        ivPhoto = findViewById(R.id.ivPhoto);
        etName = findViewById(R.id.etName);
        etHandle = findViewById(R.id.etHandle);
        etBio = findViewById(R.id.etBio);
        ivGoBack = findViewById(R.id.ivGoBack);
        ivGoBack.setOnClickListener(this);

        tvUpdate = findViewById(R.id.tvUpdate);

        ivPhoto.setOnClickListener(this);
        tvUpdate.setOnClickListener(this);

        bottomSheet = new ProfilePhotoBottomSheet();
        requestInterface = APIClient.getRetrofitInstance().create(RequestInterface.class);

        getProfileData();

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String name = etName.getText().toString();
                if (name.equals(master.getName())){
                    tvUpdate.setVisibility(View.GONE);
                } else {
                    tvUpdate.setVisibility(View.VISIBLE);
                }
            }
        });

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
                Query query = db.collection("customers");
                query.whereEqualTo("handle", handle)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.getResult().getDocuments().isEmpty()) {
                                    etHandle.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_dd_user_handle_available, 0);
                                    tvUpdate.setVisibility(View.VISIBLE);
                                } else if (handle.isEmpty()) {
                                    tvUpdate.setVisibility(View.GONE);
                                    etHandle.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
                                } else if (handle.equals(master.getHandle())) {
                                    tvUpdate.setVisibility(View.GONE);
                                    etHandle.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
                                }
                                else  {
                                    etHandle.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_dd_user_handle_unavailable, 0);
                                    tvUpdate.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });

        etBio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String bio = etBio.getText().toString();
                if (bio.equals(master.getBio())){
                    tvUpdate.setVisibility(View.GONE);
                } else {
                    tvUpdate.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void setupProfile() {
        etName.setText(master.getName());
        etHandle.setText(master.getHandle());
        if (!master.getBio().equals("Add bio")) {
            etBio.setText(master.getBio());
        }

        Glide.with(getApplicationContext())
                .load(master.getPhoto())
                .placeholder(R.drawable.dd_logo_placeholder)
                .into(ivPhoto);
    }

    @Override
    public void onButtonClicked(String text) {

    }

    @Override
    public void onClick(View v) {
        if (v == ivPhoto){
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

        if (v == tvUpdate){
            String name = etName.getText().toString();
            String handle = etHandle.getText().toString();
            String bio = etBio.getText().toString();

            HashMap profile = new HashMap();
            profile.put("name", name);
            profile.put("handle", handle);
            profile.put("bio", bio);

            Call<PublicProfile> call = requestInterface.editUserProfile(master.getId(), name, master.getEmail(), bio);
            call.enqueue(new Callback<PublicProfile>() {
                @Override
                public void onResponse(Call<PublicProfile> call, Response<PublicProfile> response) {
                    new Master(EditProfileActivity.this).setUser(response.body().getPublicProfileResponse());
                    setupProfile();
                }

                @Override
                public void onFailure(Call<PublicProfile> call, Throwable t) {
                    Log.e(TAG, "onFailure: "+t.getMessage() );
                }
            });

            Toast.makeText(this, "Profile Updated!", Toast.LENGTH_SHORT).show();
        }

        if (v == ivGoBack) {
            onBackPressed();
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

    }

    public void getProfileData(){
        Call<PublicProfile> call = requestInterface.getUserData(master.getId(), master.getId());
        call.enqueue(new Callback<PublicProfile>() {
            @Override
            public void onResponse(Call<PublicProfile> call, Response<PublicProfile> response) {
                new Master(EditProfileActivity.this).setUser(response.body().getPublicProfileResponse());
                setupProfile();
            }

            @Override
            public void onFailure(Call<PublicProfile> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage() );
            }
        });
    }

    public void closeSheet(){
        bottomSheet.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupProfile();
    }
}