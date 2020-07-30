package com.tetravalstartups.dingdong.modules.profile.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.tetravalstartups.dingdong.MainActivity;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.passbook.PassbookActivity;
import com.tetravalstartups.dingdong.modules.subscription.SubscriptionActivity;

import java.util.HashMap;
import java.util.Set;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivGoBack;
    private LinearLayout lhLogout;
    private LinearLayout lhSubscription;
    private LinearLayout lhPassbook;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initView();
    }

    private void initView() {
        ivGoBack = findViewById(R.id.ivGoBack);
        lhLogout = findViewById(R.id.lhLogout);
        lhSubscription = findViewById(R.id.lhSubscription);
        lhPassbook = findViewById(R.id.lhPassbook);

        ivGoBack.setOnClickListener(this);
        lhLogout.setOnClickListener(this);
        lhSubscription.setOnClickListener(this);
        lhPassbook.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();

//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("users")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        for (DocumentSnapshot snapshot : task.getResult()) {
//
//                            HashMap hashMap = new HashMap();
//                            hashMap.put("subscription", "0");
//
//                            db.collection("users")
//                                    .document(snapshot.getString("id"))
//                                    .collection("passbook")
//                                    .document("balance")
//                                    .update(hashMap);
//
//                            Log.e("log_video", snapshot.toString());
//
//                        }
//                    }
//                });
    }

    @Override
    public void onClick(View v) {
        if (v == ivGoBack){
            onBackPressed();
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        if (v == lhPassbook){
            startActivity(new Intent(SettingsActivity.this, PassbookActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        if (v == lhSubscription){
            startActivity(new Intent(SettingsActivity.this, SubscriptionActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        if (v == lhLogout){
            new Master(SettingsActivity.this).userLogout();
            auth.signOut();
            startActivity(new Intent(SettingsActivity.this, MainActivity.class));
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

    }
}
