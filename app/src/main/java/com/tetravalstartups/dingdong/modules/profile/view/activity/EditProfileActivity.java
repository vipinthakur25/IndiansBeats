package com.tetravalstartups.dingdong.modules.profile.view.activity;

import android.Manifest;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.api.APIClient;
import com.tetravalstartups.dingdong.api.AuthInterface;
import com.tetravalstartups.dingdong.api.RequestInterface;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.auth.Profile;
import com.tetravalstartups.dingdong.modules.profile.external.PublicProfile;
import com.tetravalstartups.dingdong.modules.profile.model.CheckHandle;
import com.tetravalstartups.dingdong.utils.ProfilePhotoBottomSheet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity implements ProfilePhotoBottomSheet.BottomSheetListener, View.OnClickListener {

    private ImageView ivPhoto;
    private EditText etName;
    private EditText etHandle;
    private EditText etEmail;
    private EditText etBio;
    private TextView tvUpdate;
    private ImageView ivGoBack;

    private Master master;

    private ProfilePhotoBottomSheet bottomSheet;
    private RequestInterface requestInterface;
    private AuthInterface authInterface;
    private Profile userProfile;
    private static final String TAG = "EditProfileActivity";

    private String blockCharacterSet = "@.~#^|$%&*!";

    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        master = new Master(EditProfileActivity.this);
        initView();
    }

    private void initView() {
        ivPhoto = findViewById(R.id.ivPhoto);
        etName = findViewById(R.id.etName);
        etHandle = findViewById(R.id.etHandle);
        etHandle.setFilters(new InputFilter[] { filter });
        etEmail = findViewById(R.id.etEmail);
        etBio = findViewById(R.id.etBio);
        ivGoBack = findViewById(R.id.ivGoBack);
        ivGoBack.setOnClickListener(this);

        tvUpdate = findViewById(R.id.tvUpdate);

        ivPhoto.setOnClickListener(this);
        tvUpdate.setOnClickListener(this);

        bottomSheet = new ProfilePhotoBottomSheet();
        requestInterface = APIClient.getRetrofitInstance().create(RequestInterface.class);
        authInterface = APIClient.getRetrofitInstance().create(AuthInterface.class);

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
                if (name.equals(master.getName())) {
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
                Call<CheckHandle> checkHandleCall = authInterface.checkHandle(master.getId(), handle);
                checkHandleCall.enqueue(new Callback<CheckHandle>() {
                    @Override
                    public void onResponse(Call<CheckHandle> call, Response<CheckHandle> response) {
                        if (handle.equals(master.getHandle())) {
                            etHandle.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
                            tvUpdate.setVisibility(View.GONE);

                        } else if (response.code() == 200) {
                            etHandle.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_dd_user_handle_unavailable, 0);
                            tvUpdate.setVisibility(View.GONE);

                        } else if (response.code() == 400) {
                            etHandle.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_dd_user_handle_available, 0);
                            tvUpdate.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<CheckHandle> call, Throwable t) {

                    }
                });
            }
        });

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String name = etEmail.getText().toString();
                if (name.equals(master.getName())) {
                    tvUpdate.setVisibility(View.GONE);
                } else {
                    tvUpdate.setVisibility(View.VISIBLE);
                }
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
                if (bio.equals(master.getBio())) {
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
        etEmail.setText(master.getEmail());

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
        if (v == ivPhoto) {
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

        if (v == tvUpdate) {
            String name = etName.getText().toString();
            String handle = etHandle.getText().toString();
            String email = etEmail.getText().toString();
            String bio = etBio.getText().toString();

            Call<PublicProfile> call = requestInterface.editUserProfile(master.getId(), name, handle, email, bio);
            call.enqueue(new Callback<PublicProfile>() {
                @Override
                public void onResponse(Call<PublicProfile> call, Response<PublicProfile> response) {
                    new Master(EditProfileActivity.this).setUser(response.body().getPublicProfileResponse());
                    setupProfile();
                }

                @Override
                public void onFailure(Call<PublicProfile> call, Throwable t) {
                    Log.e(TAG, "onFailure: " + t.getMessage());
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

    public void getProfileData() {
        Call<PublicProfile> call = requestInterface.getUserData(master.getId(), master.getId());
        call.enqueue(new Callback<PublicProfile>() {
            @Override
            public void onResponse(Call<PublicProfile> call, Response<PublicProfile> response) {
                new Master(EditProfileActivity.this).setUser(response.body().getPublicProfileResponse());
                setupProfile();
            }

            @Override
            public void onFailure(Call<PublicProfile> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void closeSheet() {
        bottomSheet.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupProfile();
    }
}