package com.tetravalstartups.dingdong;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.tetravalstartups.dingdong.auth.LoginActivity;
import com.tetravalstartups.dingdong.auth.SignUpActivity;

public class ComingSoonActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvCount;
    private TextView tvJoin;
    private TextView tvLogout;

    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coming_soon);
        initView();

    }

    private void initView() {
        tvCount = findViewById(R.id.tvCount);

        tvJoin = findViewById(R.id.tvJoin);
        tvJoin.setOnClickListener(this);

        tvLogout = findViewById(R.id.tvLogout);
        tvLogout.setOnClickListener(this);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null){
            db.collection("users")
                    .document(firebaseAuth.getCurrentUser().getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                String handle = task.getResult().getString("handle");
                                tvJoin.setText(handle);
                            }
                        }
                    });
            tvJoin.setEnabled(false);
            tvLogout.setVisibility(View.VISIBLE);

        } else {
            tvJoin.setEnabled(true);
            tvJoin.setText("Join me in");
            tvLogout.setVisibility(View.GONE);
        }

        fetchPeoplesCount();
    }

    public void fetchPeoplesCount(){
        db.collection("users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (!documentSnapshots.getDocuments().isEmpty()){
                            String count = String.valueOf(documentSnapshots.size());
                            tvCount.setText(String.format("#%s", count));
                        }
                    }
                });

    }

    @Override
    public void onClick(View v) {
        if (v == tvJoin){
            startActivity(new Intent(ComingSoonActivity.this, LoginActivity.class));
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        if (v == tvLogout){
            firebaseAuth.signOut();
            startActivity(new Intent(ComingSoonActivity.this, ComingSoonActivity.class));
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }
}
