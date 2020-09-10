package com.tetravalstartups.dingdong.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.tetravalstartups.dingdong.api.APIClient;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.api.RequestInterface;
import com.tetravalstartups.dingdong.modules.profile.model.DeleteVideoResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DDDeleteVideoAlert {

    public static DDDeleteVideoAlert ddAlert = null;
    private Dialog mDialog;
    private RequestInterface requestInterface;
    private static final String TAG = "DDDeleteVideoAlert";


    public static DDDeleteVideoAlert getInstance() {
        if (ddAlert == null){
            ddAlert = new DDDeleteVideoAlert();
        }
        return ddAlert;
    }

    public void showAlert(Context context, String cover_url, String video_id, boolean cancelable) {
        mDialog = new Dialog(context, R.style.DDLoading);
        mDialog.setContentView(R.layout.dd_alert_video_delete_layout);
        requestInterface = APIClient.getRetrofitInstance().create(RequestInterface.class);

        TextView tvDDTitle = mDialog.findViewById(R.id.tvDDTitle);
        ImageView ivVideoCover = mDialog.findViewById(R.id.ivVideoCover);
        TextView tvDelete = mDialog.findViewById(R.id.tvDelete);
        TextView tvCancel = mDialog.findViewById(R.id.tvCancel);

        tvDDTitle.setText("Delete Video?");
        Glide.with(context).load(cover_url).placeholder(R.drawable.dd_logo_placeholder).into(ivVideoCover);

        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<DeleteVideoResponse> call = requestInterface.deleteUserVideo(video_id);
                call.enqueue(new Callback<DeleteVideoResponse>() {
                    @Override
                    public void onResponse(Call<DeleteVideoResponse> call, Response<DeleteVideoResponse> response) {
                        if (response.code() == 200) {
                            Intent intent = new Intent("deleteVideo");
                            intent.putExtra("code", "200");
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                            mDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<DeleteVideoResponse> call, Throwable t) {
                        Log.e(TAG, "onFailure: "+t.getMessage() );
                    }
                });
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        mDialog.setCancelable(cancelable);
        mDialog.setCanceledOnTouchOutside(cancelable);
        mDialog.show();
    }

    public void hideAlert() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
