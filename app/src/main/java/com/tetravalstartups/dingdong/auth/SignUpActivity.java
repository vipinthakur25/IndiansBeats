package com.tetravalstartups.dingdong.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.utils.Constant;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivGoBack;
    private ProgressBar progressBar;
    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private TextView tvSignUp;
    private LinearLayout lhLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initView();
    }

    private void initView() {
        ivGoBack = findViewById(R.id.ivGoBack);
        progressBar = findViewById(R.id.progressBar);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etName = findViewById(R.id.etName);
        tvSignUp = findViewById(R.id.tvSignUp);
        lhLogin = findViewById(R.id.lhLogin);

        ivGoBack.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
        lhLogin.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v == ivGoBack){
            onBackPressed();
            finish();
        }

        if (v == tvSignUp){
            doUiValidation();
        }

        if (v == lhLogin){
            onBackPressed();
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    private void doUiValidation() {
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (TextUtils.isEmpty(name)){
            etName.requestFocus();
            etName.setError("Name is required");
            return;
        }

        if (TextUtils.isEmpty(email)){
            etEmail.requestFocus();
            etEmail.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(password)){
            etPassword.requestFocus();
            etPassword.setError("Password is required");
            return;
        }

        if (password.length() < 6){
            etPassword.requestFocus();
            etPassword.setError("Minimum six characters are required");
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        tvSignUp.setEnabled(false);
        etName.setEnabled(false);
        etEmail.setEnabled(false);
        etPassword.setEnabled(false);
       // doSignUp(name, email, password);
    }

//    private void doSignUp(final String name, final String email, final String password) {
//        final FirebaseAuth auth = FirebaseAuth.getInstance();
//        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()){
//                    FirebaseFirestore db = FirebaseFirestore.getInstance();
//                    Profile profile = new Profile();
//                    String[] handle = email.split("@");
//                    profile.setId(auth.getCurrentUser().getUid());
//                    profile.setName(name);
//                    profile.setEmail(email);
//                    profile.setPhoto(Constant.DD_IV_PLACEHOLDER);
//                    profile.setBio(Constant.DEFAULT_BIO);
//                    profile.setHandle(handle[0]);
//                    profile.setLikes(Constant.INITIAL_LIKES);
//                    profile.setFollowers(Constant.INITIAL_FOLLOWER);
//                    profile.setFollowing(Constant.INITIAL_FOLLOWING);
//
//                    HashMap hashMap = new HashMap();
//                    hashMap.put("reserved", "0");
//                    hashMap.put("unreserved", "0");
//                    hashMap.put("cashback", "0");
//                    hashMap.put("video", "0");
//                    hashMap.put("subscription", "0");
//
//
//
//                    db.collection("users")
//                            .document(auth.getCurrentUser().getUid())
//                            .set(profile)
//                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()){
//
//                                        db.collection("users")
//                                                .document(auth.getCurrentUser().getUid())
//                                                .collection("passbook")
//                                                .document("balance")
//                                                .set(hashMap)
//                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                    @Override
//                                                    public void onComplete(@NonNull Task<Void> task) {
//
//                                                        tvSignUp.setEnabled(true);
//                                                        etName.setEnabled(true);
//                                                        etEmail.setEnabled(true);
//                                                        etPassword.setEnabled(true);
//                                                        progressBar.setVisibility(View.INVISIBLE);
//                                                        auth.signOut();
//                                                        onBackPressed();
//                                                        finish();
//                                                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//
//                                                    }
//                                                });
//                                    }
//                                }
//                            });
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                tvSignUp.setEnabled(true);
//                etName.setEnabled(true);
//                etEmail.setEnabled(true);
//                etPassword.setEnabled(true);
//                progressBar.setVisibility(View.INVISIBLE);
//                Toast.makeText(SignUpActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}
