package com.tetravalstartups.dingdong.modules.profile.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.api.APIClient;
import com.tetravalstartups.dingdong.api.AuthInterface;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.profile.model.MyComplain;
import com.tetravalstartups.dingdong.modules.profile.model.ReportAProblem;
import com.tetravalstartups.dingdong.modules.profile.presenter.MyComplainPresenter;
import com.tetravalstartups.dingdong.modules.profile.view.adapter.MyComplainAdapter;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportAProblemActivity extends AppCompatActivity implements View.OnClickListener, MyComplainPresenter.IComplainRequest {
    private ImageView ivGoBack;
    private EditText etIssue;
    private Uri imageuri;
    private ImageView ivAddPhoto;
    private ImageView ivScreenShot;
    private LinearLayout lvAddIssue;
    private TextView tvSubmitProblem;

    private TextView tvAddScreenshot;
    private int REQUEST_CODE = 1;
    private FirebaseStorage storage;
    private static final String TAG = "ReportAProblemActivity";
    private String fileUrl;
    private Master master;
    private AuthInterface authInterface;
    private RecyclerView RAPRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_a_problem);

        initViews();
    }

    private void initViews() {
        ivGoBack = findViewById(R.id.ivGoBack);
        ivScreenShot = findViewById(R.id.ivScreenShot);
        etIssue = findViewById(R.id.etIssue);
        RAPRecyclerView = findViewById(R.id.RAPRecyclerView);
        ivAddPhoto = findViewById(R.id.ivAddPhoto);
        lvAddIssue = findViewById(R.id.lvAddIssue);
        tvSubmitProblem = findViewById(R.id.tvSubmitProblem);
        tvAddScreenshot = findViewById(R.id.tvAddScreenshot);
        storage = FirebaseStorage.getInstance();
        RAPRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        master = new Master(ReportAProblemActivity.this);
        authInterface = APIClient.getRetrofitInstance().create(AuthInterface.class);
        ivGoBack.setOnClickListener(this);
        lvAddIssue.setOnClickListener(this);
        tvSubmitProblem.setOnClickListener(this);


        etIssue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String issue = etIssue.getText().toString();
                if (issue.isEmpty() || issue.length() < 10) {
                    tvSubmitProblem.setBackground(getDrawable(R.drawable.bg_button_disabled));
                    tvSubmitProblem.setTextColor(getResources().getColor(R.color.colorDisableText));
                    tvSubmitProblem.setEnabled(false);
                } else {
                    tvSubmitProblem.setBackground(getDrawable(R.drawable.bg_button_gradient));
                    tvSubmitProblem.setTextColor(getResources().getColor(R.color.colorTextTitle));
                    tvSubmitProblem.setEnabled(true);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        fetchMyComplain();
    }

    private void fetchMyComplain() {
        MyComplainPresenter myComplainPresenter = new MyComplainPresenter(ReportAProblemActivity.this,
                ReportAProblemActivity.this);
        myComplainPresenter.fetchComplain();
    }

    @Override
    public void onClick(View view) {
        if (view == ivGoBack) {
            onBackPressed();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        if (view == lvAddIssue) {
            chooseImage();
        }
        if (view == tvSubmitProblem) {
            uploadProblem();
        }
    }

    private void uploadProblem() {
        String textIssue = etIssue.getText().toString();
        String id = master.getId();
        if (textIssue.isEmpty()) {
            etIssue.requestFocus();
            etIssue.setError("Issue required");
            return;
        }
        if (imageuri == null) {
            lvAddIssue.requestFocus();
            lvAddIssue.setBackground(getResources().getDrawable(R.drawable.dashed_issue_error_bg));
            tvAddScreenshot.setTextColor(getResources().getColor(R.color.colorRed));
            ivScreenShot.setImageResource(R.drawable.dd_error);
            return;
        }
        StorageReference reference = storage.getReference();
        StorageReference imageFolder = reference.child("problem");

        StorageReference imageRef = imageFolder.child("images/"
                + UUID.randomUUID().toString());
        imageRef.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(ReportAProblemActivity.this, "Succeeded", Toast.LENGTH_SHORT).show();
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    // getting image uri and converting into string
                    Uri downloadUrl = uri;
                    fileUrl = downloadUrl.toString();
                    Log.e(TAG, "onSuccess: " + fileUrl);
                    sendImageUrlToApi(id, textIssue, fileUrl);
                });
            }

            private void sendImageUrlToApi(String id, String text_issue, String fileUrl) {

                Call<ReportAProblem> reportAProblemCall = authInterface.reportProblem(id, text_issue, fileUrl, "", "");
                reportAProblemCall.enqueue(new Callback<ReportAProblem>() {
                    @Override
                    public void onResponse(Call<ReportAProblem> call, Response<ReportAProblem> response) {
                        if (response.code() == 200) {
                            Toast.makeText(ReportAProblemActivity.this, "Uploaded to api", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ReportAProblem> call, Throwable t) {
                        Toast.makeText(ReportAProblemActivity.this, "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ReportAProblemActivity.this, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            imageuri = data.getData();
            Glide.with(ReportAProblemActivity.this).load(imageuri).into(ivAddPhoto);
            ivAddPhoto.setVisibility(View.VISIBLE);
            lvAddIssue.setVisibility(View.GONE);
            ivScreenShot.setVisibility(View.GONE);
            tvAddScreenshot.setVisibility(View.GONE);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void fetchResponse(MyComplain myComplain) {
        MyComplainAdapter myComplainAdapter = new MyComplainAdapter(ReportAProblemActivity.this, myComplain.getData());
        RAPRecyclerView.setAdapter(myComplainAdapter);
    }

    @Override
    public void fetchError(String error) {
        Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
    }
}
