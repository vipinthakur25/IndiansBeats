package com.tetravalstartups.dingdong.modules.profile.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.tetravalstartups.dingdong.MainActivity;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.api.APIClient;
import com.tetravalstartups.dingdong.api.AuthInterface;
import com.tetravalstartups.dingdong.api.PlanInterface;
import com.tetravalstartups.dingdong.api.UserInterface;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.passbook.PassbookActivity;
import com.tetravalstartups.dingdong.modules.passbook.model.BanksResponse;
import com.tetravalstartups.dingdong.modules.passbook.model.UpdatePassbook;
import com.tetravalstartups.dingdong.modules.subscription.SubscriptionActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    int index = 0;
    private ImageView ivGoBack;
    private LinearLayout lhLogout;
    private LinearLayout lhSubscription;
    private LinearLayout lhPassbook;
    private LinearLayout lhTAndC;
    private LinearLayout lhSecurity;
    private FirebaseAuth auth;

    private static final String TAG = "SettingsActivity";

    private PlanInterface planInterface;
    private AuthInterface authInterface;
    private UserInterface userInterface;

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
        lhTAndC = findViewById(R.id.lhTAndC);
        lhSecurity = findViewById(R.id.lhSecurity);

        ivGoBack.setOnClickListener(this);
        lhLogout.setOnClickListener(this);
        lhSubscription.setOnClickListener(this);
        lhPassbook.setOnClickListener(this);
        lhTAndC.setOnClickListener(this);
        lhSecurity.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
        planInterface = APIClient.getRetrofitInstance().create(PlanInterface.class);
        authInterface = APIClient.getRetrofitInstance().create(AuthInterface.class);
        userInterface = APIClient.getRetrofitInstance().create(UserInterface.class);

    }

    @Override
    public void onClick(View v) {
        if (v == ivGoBack) {
            onBackPressed();
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        if (v == lhPassbook) {
            startActivity(new Intent(SettingsActivity.this, PassbookActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        if (v == lhSubscription) {
            startActivity(new Intent(SettingsActivity.this, SubscriptionActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        if (v == lhLogout) {
            new Master(SettingsActivity.this).userLogout();
            auth.signOut();
            startActivity(new Intent(SettingsActivity.this, MainActivity.class));
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        if (v == lhTAndC) {
            startActivity(new Intent(SettingsActivity.this, TAndCActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        if (v == lhSecurity) {
            fetchUsers();
            //fetchTransactions();
//            fetchSubscriptions();
        }

    }


//    private void fetchTransactions() {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("customers")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> taskS) {
//                        for (DocumentSnapshot snapshot : taskS.getResult()) {
//                            db.collection("customers")
//                                    .document(snapshot.getString("id"))
//                                    .collection("transactions")
//                                    .get()
//                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<QuerySnapshot> task1) {
//                                            if (task1.isSuccessful()) {
//                                                if (!task1.getResult().getDocuments().isEmpty()) {
//                                                    for (DocumentSnapshot snapshot1 : task1.getResult()) {
//                                                        Transactions transactions = snapshot1.toObject(Transactions.class);
//                                                        updateTransaction(transactions, snapshot.getString("id"));
//                                                    }
//                                                }
//                                            }
//                                        }
//                                    });
//                        }
//                    }
//                });
//    }

//    private void fetchSubscriptions() {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("customers")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> taskS) {
//                        for (DocumentSnapshot snapshot : taskS.getResult()) {
//                            db.collection("customers")
//                                    .document(snapshot.getString("id"))
//                                    .collection("subscription")
//                                    .get()
//                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<QuerySnapshot> task1) {
//                                            if (task1.isSuccessful()) {
//                                                if (!task1.getResult().getDocuments().isEmpty()) {
//                                                    for (DocumentSnapshot snapshot1 : task1.getResult()) {
//                                                        Subscribed subscribed = snapshot1.toObject(Subscribed.class);
//                                                        updateSubscriptions(subscribed, snapshot.getString("id"));
//                                                    }
//                                                }
//                                            }
//                                        }
//                                    });
//                        }
//                    }
//                });
//    }

//    private void updateTransaction(Transactions transactions, String id) {
//        Call<TransactionResponse> call = planInterface.performTransaction(
//                transactions.getId(),
//                id,
//                transactions.getAmount(),
//                transactions.getRemark(),
//                transactions.getDate(),
//                transactions.getTime(),
//                Integer.parseInt(transactions.getStatus())
//        );
//
//        call.enqueue(new Callback<TransactionResponse>() {
//            @Override
//            public void onResponse(Call<TransactionResponse> call, Response<TransactionResponse> response) {
//                if (response.code() == 200) {
//                    Log.e(TAG, "onResponse: with transaction: "+transactions.getId()+" for user: "+id);
//                } else if (response.code() == 400){
//                    Log.e(TAG, "onResponse: transaction: "+transactions.getId()+" already exist for user" );
//                } else {
//                    Toast.makeText(SettingsActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<TransactionResponse> call, Throwable t) {
//                Log.e(TAG, "onFailure: "+t.getMessage());
//            }
//        });
//
//    }

//    private void updateSubscriptions(Subscribed subscribed, String id) {
//        Call<SubscriptionResponse> call = planInterface.addSubs(
//                subscribed.getId(),
//                subscribed.getName(),
//                subscribed.getTotal_uploads(),
//                subscribed.getAvl_uploads(),
//                subscribed.getStart_date(),
//                subscribed.getEnd_date(),
//                subscribed.getMonthly_profit(),
//                subscribed.getStatus(),
//                id
//        );
//
//        call.enqueue(new Callback<SubscriptionResponse>() {
//            @Override
//            public void onResponse(Call<SubscriptionResponse> call, Response<SubscriptionResponse> response) {
//                if (response.code() == 200) {
//                    Log.e(TAG, "onResponse: with subscription: "+subscribed.getId()+" for user: "+id);
//                } else if (response.code() == 400){
//                    Log.e(TAG, "onResponse: subscription: "+subscribed.getId()+" already exist for user" );
//                } else {
//                    Toast.makeText(SettingsActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<SubscriptionResponse> call, Throwable t) {
//                Log.e(TAG, "onFailure: "+t.getMessage() );
//            }
//        });
//
//    }


    private void fetchUsers() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("customers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot snapshot : task.getResult()) {
                                String user_id = snapshot.getString("id");
                                fetchBanks(user_id);
                            }
                        }
                    }
                });
    }

    private void fetchBanks(String user_id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("customers")
                .document(user_id)
                .collection("banks")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot snapshot : task.getResult()) {
                                String id = snapshot.getString("id");
                                String account = snapshot.getString("account");
                                String branch = snapshot.getString("branch");
                                String ifsc = snapshot.getString("ifsc");
                                String address = snapshot.getString("address");
                                updateBank(id, user_id, account, address, branch, ifsc, "-1");
                            }
                        }
                    }
                });
    }

    private void updateBank(String id, String user_id, String account, String address, String branch, String ifsc, String status) {
        Call<BanksResponse> call = userInterface.updateBank(
                id,
                user_id,
                account,
                address,
                branch,
                ifsc,
                status
        );

        call.enqueue(new Callback<BanksResponse>() {
            @Override
            public void onResponse(Call<BanksResponse> call, Response<BanksResponse> response) {
                if (response.code() == 200) {
                    Log.e(TAG, "onResponse: for user"+user_id+" with bank id"+id+response.message());
                }  else {
                    Log.e(TAG, "onResponse: with bank id"+id+" exist"+response.message());
                }
            }

            @Override
            public void onFailure(Call<BanksResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage() );
            }
        });

    }


//    private void generatePassbook(String user_id) {
//        Call<GeneratePassbook> call = authInterface.generatePassbook(user_id, user_id);
//        call.enqueue(new Callback<GeneratePassbook>() {
//            @Override
//            public void onResponse(Call<GeneratePassbook> call, Response<GeneratePassbook> response) {
//                if (response.code() == 200) {
//                    Log.e(TAG, "onResponse: for user: "+user_id+" "+response.message());
//                    fetchBalances(user_id);
//                } else if (response.code() == 400){
//                    Log.e(TAG, "onResponse: for user: "+user_id+" "+response.message());
//                } else {
//                    Log.e(TAG, "onResponse: Something went wrong...");
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<GeneratePassbook> call, Throwable t) {
//                Log.e(TAG, "onFailure: "+t.getMessage());
//            }
//        });
//    }

    private void fetchBalances(String user_id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("customers")
                .document(user_id)
                .collection("passbook")
                .document("balance")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        String daily_rewards = task.getResult().getString("daily_rewards");
                        String fans_donation = task.getResult().getString("fans_donation");
                        String subscription = task.getResult().getString("subscription");
                        String time_spent = task.getResult().getString("time_spent");
                        String video_uploads = task.getResult().getString("video_uploads");
                        updateBalances(user_id, daily_rewards, fans_donation, subscription, time_spent, video_uploads);
                    }
                });
    }

    private void updateBalances(String user_id, String daily_rewards, String fans_donation, String subscription, String time_spent, String video_uploads) {
        Call<UpdatePassbook> call = userInterface.updatePassbook(
                user_id,
                daily_rewards,
                fans_donation,
                subscription,
                time_spent,
                video_uploads
        );

        call.enqueue(new Callback<UpdatePassbook>() {
            @Override
            public void onResponse(Call<UpdatePassbook> call, Response<UpdatePassbook> response) {
                if (response.code() == 200) {
                    Log.e(TAG, "onResponse: for user: " + user_id + " " + response.message());
                } else if (response.code() == 400) {
                    Log.e(TAG, "onResponse: for user: " + user_id + " " + response.message());
                } else {
                    Log.e(TAG, "onResponse: Something went wrong...");
                }
            }

            @Override
            public void onFailure(Call<UpdatePassbook> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

}
