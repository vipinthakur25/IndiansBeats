package com.tetravalstartups.dingdong.service;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tetravalstartups.dingdong.api.APIClient;
import com.tetravalstartups.dingdong.api.PlanInterface;
import com.tetravalstartups.dingdong.api.RequestInterface;
import com.tetravalstartups.dingdong.modules.passbook.Transactions;
import com.tetravalstartups.dingdong.modules.passbook.transactions.model.TransactionResponse;
import com.tetravalstartups.dingdong.modules.publish.PublishMeta;
import com.tetravalstartups.dingdong.modules.publish.UploadVideoResponse;
import com.tetravalstartups.dingdong.modules.publish.UseTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PublishService extends Service {

    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private PublishMeta publishMeta;
    private RequestInterface requestInterface;
    private PlanInterface planInterface;

    private static final String TAG = "PublishService";

    @Override
    public void onCreate() {
        super.onCreate();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        requestInterface = APIClient.getRetrofitInstance().create(RequestInterface.class);
        planInterface = APIClient.getRetrofitInstance().create(PlanInterface.class);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        publishMeta = new PublishMeta();
        publishMeta = intent.getParcelableExtra("meta");
        doStartUpload(publishMeta);
        return START_STICKY;
    }


    private void doStartUpload(PublishMeta publishMeta) {

        Intent sendBroadcastIntent = new Intent("publishService");
        sendBroadcastIntent.putExtra("status", "publishing");
        sendBroadcastIntent.putExtra("thumbnail", publishMeta.getVideo_thumbnail());

        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(sendBroadcastIntent);
        if (publishMeta.getVideo_path() != null) {
            InputStream stream = null;
            try {
                stream = new FileInputStream(new File(publishMeta.getVideo_path()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            StorageReference ref = storageRef.child("data/videos/" + UUID.randomUUID().toString());
            assert stream != null;
            ref.putStream(stream)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        String video_url = task.getResult().toString();
                                        uploadThumbnail(video_url, publishMeta);
                                    }
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
        }
    }

    private void uploadThumbnail(String video_url, PublishMeta publishMeta) {
        if (publishMeta.getVideo_thumbnail() != null) {
            InputStream stream = null;
            try {
                stream = new FileInputStream(new File(publishMeta.getVideo_thumbnail()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            StorageReference ref = storageRef.child("data/thumbnails/" + UUID.randomUUID().toString());
            assert stream != null;
            ref.putStream(stream)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        String thumbnail_url = task.getResult().toString();
                                        setVideoMeta(video_url, thumbnail_url, publishMeta);
                                    }
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
        }
    }

    private void setVideoMeta(String video_url, String thumbnail_url, PublishMeta publishMeta) {
        Call<UploadVideoResponse> call = requestInterface.uploadVideo(
                publishMeta.getId(),
                publishMeta.getSound_id(),
                publishMeta.getSound_title(),
                publishMeta.getUser_id(),
                video_url,
                thumbnail_url,
                publishMeta.getVideo_status(),
                publishMeta.getVideo_desc(),
                publishMeta.getVideo_index()
        );
        call.enqueue(new Callback<UploadVideoResponse>() {
            @Override
            public void onResponse(Call<UploadVideoResponse> call, Response<UploadVideoResponse> response) {
                if (response.code() == 200) {
                    checkForRewardType();
                }
            }

            @Override
            public void onFailure(Call<UploadVideoResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void checkForRewardType() {
        int type = publishMeta.getReward_type();
        if (type == 1) {
            calculateRewardAmount(type);
        }
        if (type == 2) {
            updateOrganicBalance(type);
        }
    }

    private void calculateRewardAmount(int type) {
        int per_day = publishMeta.getMonthly_profit() / 30;
        int per_video = per_day / publishMeta.getTotal_uploads();
        updateSubsBalance(type, per_video, "Subscription Reward");
    }

    private void updateSubsBalance(int type, int amount, String remark) {
        Call<UseTask> call = planInterface.doUseTask(
                type,
                publishMeta.getUser_id(),
                publishMeta.getSubs_id(),
                amount
        );

        call.enqueue(new Callback<UseTask>() {
            @Override
            public void onResponse(Call<UseTask> call, Response<UseTask> response) {
                if (response.code() == 200) {
                    updateTransactions(amount, remark);
                } else if (response.code() == 400) {
                    Log.e(TAG, "onResponse: "+response.message());
                } else if (response.code() == 500) {
                    Toast.makeText(PublishService.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UseTask> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage() );
            }
        });
    }

    private void updateOrganicBalance(int type) {
        Call<UseTask> call = planInterface.doUseTask(
                type,
                publishMeta.getUser_id(),
                "0",
                10
        );

        call.enqueue(new Callback<UseTask>() {
            @Override
            public void onResponse(Call<UseTask> call, Response<UseTask> response) {
                if (response.code() == 200) {
                    updateTransactions(10, "Video Upload Reward");
                } else if (response.code() == 400) {
                    Log.e(TAG, "onResponse: "+response.message());
                } else if (response.code() == 500) {
                    Toast.makeText(PublishService.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UseTask> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage() );
            }
        });

    }

    private void updateTransactions(int amount, String remark) {
        DocumentReference documentReference = db.collection("videos").document();
        String txn_id = documentReference.getId();
        Call<TransactionResponse> call = planInterface.performTransaction(
                txn_id,
                publishMeta.getUser_id(),
                String.valueOf(amount),
                remark,
                getCurrentDate(),
                getCurrentTime(),
                2
        );

        call.enqueue(new Callback<TransactionResponse>() {
            @Override
            public void onResponse(Call<TransactionResponse> call, Response<TransactionResponse> response) {
                if (response.code() == 200) {
                    Intent intent = new Intent("publishService");
                    intent.putExtra("status", "published");
                    intent.putExtra("thumbnail", publishMeta.getVideo_thumbnail());
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                } else {
                    Log.e(TAG, "onResponse: "+response.message());
                }
            }

            @Override
            public void onFailure(Call<TransactionResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage());
            }
        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent("publishService");
        intent.putExtra("status", "published");
        intent.putExtra("thumbnail", publishMeta.getVideo_thumbnail());
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private String getCurrentDate() {
        DateFormat dfDate = new SimpleDateFormat("d MMM yyyy");
        String txnDate = dfDate.format(Calendar.getInstance().getTime());
        return txnDate;
    }

    private String getCurrentTime() {
        DateFormat dfTime = new SimpleDateFormat("h:mm a");
        String txnTime = dfTime.format(Calendar.getInstance().getTime());
        return txnTime;
    }

}
