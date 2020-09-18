package com.tetravalstartups.dingdong.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tetravalstartups.dingdong.api.APIClient;
import com.tetravalstartups.dingdong.MainActivity;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.api.AuthInterface;
import com.tetravalstartups.dingdong.api.RequestInterface;
import com.tetravalstartups.dingdong.modules.passbook.model.GeneratePassbook;
import com.tetravalstartups.dingdong.modules.profile.external.PublicProfileResponse;
import com.tetravalstartups.dingdong.utils.Constant;
import com.tetravalstartups.dingdong.utils.DDLoading;
import com.tetravalstartups.dingdong.utils.ProfilePhotoBottomSheet;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetupProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etPhone;
    private EditText etName;
    private EditText etEmail;
    private EditText etHandle;
    private EditText etBio;
    private TextView tvUpdate;
    private ImageView ivGoBack;

    private LinearLayout lvProfile;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    private ProfilePhotoBottomSheet bottomSheet;
    private RequestInterface requestInterface;
    private AuthInterface authInterface;
    private DDLoading ddLoading;

    private static final String TAG = "SetupProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);
        initView();
    }

    private void initView() {
        etPhone = findViewById(R.id.etPhone);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etHandle = findViewById(R.id.etHandle);
        etBio = findViewById(R.id.etBio);
        ivGoBack = findViewById(R.id.ivGoBack);
        lvProfile = findViewById(R.id.lvProfile);
        ivGoBack.setOnClickListener(this);

        tvUpdate = findViewById(R.id.tvUpdate);
        tvUpdate.setOnClickListener(this);

        bottomSheet = new ProfilePhotoBottomSheet();
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        ddLoading = DDLoading.getInstance();

        etPhone.setText(firebaseAuth.getCurrentUser().getPhoneNumber());
        requestInterface = APIClient.getRetrofitInstance().create(RequestInterface.class);
        authInterface = APIClient.getRetrofitInstance().create(AuthInterface.class);

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

        if (view == tvUpdate) {
            String name = etName.getText().toString();
            String email = etEmail.getText().toString();
            String bio = etBio.getText().toString();

            if (TextUtils.isEmpty(name)) {
                etName.setError("Full name is required");
                return;
            }

            if (TextUtils.isEmpty(email)) {
                etEmail.setError("Email is required");
                return;
            }

            if (!isValidEmail(email)) {
                etEmail.setError("Email is not valid");
                return;
            }

            if (TextUtils.isEmpty(bio)) {
                Toast.makeText(this, "Bio required", Toast.LENGTH_SHORT).show();
                return;
            }

            String[] handle = email.split("@");

            if (!isConnected()) {
                Snackbar.make(lvProfile, "Please make sure that you are connect to internet.", Snackbar.LENGTH_LONG).show();
            } else {
                ddLoading.showProgress(SetupProfileActivity.this, "Hold On...", false);
                setProfileData(name, email, handle[0], bio);
            }
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private void setProfileData(String name, String email, String handle, String bio) {
        PublicProfileResponse profile = new PublicProfileResponse();
        profile.setId(firebaseAuth.getCurrentUser().getUid());
        profile.setName(name);
        profile.setEmail(email);
        profile.setPhoto(Constant.DD_IV_PLACEHOLDER);
        profile.setBio(bio);
        profile.setHandle(handle);
        profile.setLikes(0);
        profile.setFollowers(0);
        profile.setFollowing(0);

        HashMap hashMap = new HashMap();
        hashMap.put("daily_rewards", "0");
        hashMap.put("time_spent", "0");
        hashMap.put("video_uploads", "0");
        hashMap.put("fans_donation", "0");
        hashMap.put("subscription", "0");


        db.collection("customers")
                .document(firebaseAuth.getCurrentUser().getUid())
                .set(profile)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            db.collection("customers")
                                    .document(firebaseAuth.getCurrentUser().getUid())
                                    .collection("passbook")
                                    .document("balance")
                                    .set(hashMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            String id = firebaseAuth.getCurrentUser().getUid();
                                            String authPhone = firebaseAuth.getCurrentUser().getPhoneNumber();
                                            authPhone = authPhone.replace("+91", "");
                                            signUpUser(id, handle, name, email, authPhone);
                                        }
                                    });
                        }
                    }
                });

    }

    private void signUpUser(String id, String handle, String name, String email, String authPhone) {
        Call<SignUp> call = requestInterface.signUpUser(id, handle, name, email, authPhone);
        call.enqueue(new Callback<SignUp>() {
            @Override
            public void onResponse(Call<SignUp> call, Response<SignUp> response) {
                if (response.code() == 200) {
                    Log.e(TAG, "onResponse: "+response.message());
                    generatePassbook(id);
                } else {
                    ddLoading.hideProgress();
                    Log.e(TAG, "onResponse: "+response.message());
                    Snackbar.make(lvProfile, "Something went wrong...", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SignUp> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage() );
            }
        });
    }

        private void generatePassbook(String user_id) {
        Call<GeneratePassbook> call = authInterface.generatePassbook(user_id, user_id);
        call.enqueue(new Callback<GeneratePassbook>() {
            @Override
            public void onResponse(Call<GeneratePassbook> call, Response<GeneratePassbook> response) {
                ddLoading.hideProgress();
                if (response.code() == 200) {
                    goToHome();
                    Log.e(TAG, "onResponse: for user: "+user_id+" "+response.message());
                } else if (response.code() == 400){
                    Log.e(TAG, "onResponse: for user: "+user_id+" "+response.message());
                } else {
                    Log.e(TAG, "onResponse: Something went wrong...");
                }

            }

            @Override
            public void onFailure(Call<GeneratePassbook> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage());
            }
        });
    }


    private void goToHome() {
        Intent intent = new Intent(SetupProfileActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}