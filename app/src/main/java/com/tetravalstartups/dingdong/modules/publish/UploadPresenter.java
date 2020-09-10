package com.tetravalstartups.dingdong.modules.publish;

import android.content.Context;
import android.widget.Toast;

public class UploadPresenter {
    private Context context;
    private IUpload iUpload;

    public UploadPresenter(Context context, IUpload iUpload) {
        this.context = context;
        this.iUpload = iUpload;
    }

    public interface IUpload {
        void uploadSuccess();
        void uploadFailed();
    }

    public void uploadVideo(String video_path) {
        Toast.makeText(context, "Uploading video...", Toast.LENGTH_SHORT).show();
    }
}
