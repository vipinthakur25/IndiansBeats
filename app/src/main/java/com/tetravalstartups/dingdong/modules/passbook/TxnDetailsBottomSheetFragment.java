package com.tetravalstartups.dingdong.modules.passbook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.tetravalstartups.dingdong.MainActivity;
import com.tetravalstartups.dingdong.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class TxnDetailsBottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private View view;
    private TxnDetailsListener txnDetailsListener;

    private ImageView ivShare;
    private TextView tvRemark;
    private TextView tvAmount;
    private TextView tvTimeDate;
    private TextView tvId;
    private TextView tvStatus;
    private ImageView ivStatus;

    private LinearLayout lvTxnDetails;

    private File imagePath;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.txn_details_bottom_sheet_layout, container, false);
        initView();
        return view;
    }

    private void initView() {

        ivShare = view.findViewById(R.id.ivShare);
        tvRemark = view.findViewById(R.id.tvRemark);
        tvAmount = view.findViewById(R.id.tvAmount);
        tvTimeDate = view.findViewById(R.id.tvTimeDate);
        tvId = view.findViewById(R.id.tvId);
        tvStatus = view.findViewById(R.id.tvStatus);
        ivStatus = view.findViewById(R.id.ivStatus);

        lvTxnDetails = view.findViewById(R.id.lvTxnDetails);

        setupTxnDetailsSheet();

    }

    private void setupTxnDetailsSheet() {
        SharedPreferences preferences = getContext().getSharedPreferences("unreserved", 0);
        String id = preferences.getString("id", "");
        int type = preferences.getInt("type",-1);
        int amount = preferences.getInt("amount", -1);
        String time = preferences.getString("time", "");
        String date = preferences.getString("date", "");
        String remark = preferences.getString("remark", "");
        int status = preferences.getInt("status",-1);

        tvRemark.setText(remark);
        tvAmount.setText(amount+"");
        tvTimeDate.setText(time+" ~ "+date);
        tvId.setText(id);

        if (status == 0){
            ivStatus.setBackground(getResources().getDrawable(R.drawable.bg_red_circle));
            ivStatus.setImageDrawable(getResources().getDrawable(R.drawable.ic_dd_check_mark_white));
            tvStatus.setText("Pending");
        } else if (status == 1){
            ivStatus.setBackground(getResources().getDrawable(R.drawable.bg_yellow_circle));
            ivStatus.setImageDrawable(getResources().getDrawable(R.drawable.ic_dd_check_mark_white));
            tvStatus.setText("Processing");
        } else if (status == 2){
            ivStatus.setBackground(getResources().getDrawable(R.drawable.bg_green_circle));
            ivStatus.setImageDrawable(getResources().getDrawable(R.drawable.ic_dd_check_mark_white));
            tvStatus.setText("Successful");
        } else if (status == 3){
            ivStatus.setBackground(getResources().getDrawable(R.drawable.bg_red_circle));
            ivStatus.setImageDrawable(null);
            tvStatus.setText("Rejected");
        } else if (status == 4){
            ivStatus.setBackground(getResources().getDrawable(R.drawable.bg_red_circle));
            ivStatus.setImageDrawable(null);
            tvStatus.setText("Hold");
        }

        ivShare.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v == ivShare){
            LinearLayout rootView = view.findViewById(R.id.lvTxnDetails);
            Bitmap screenshot = takeScreenshot(rootView);
            saveBitmap(screenshot);
            shareIt();
        }
    }

    private Bitmap takeScreenshot(View view) {
        View v = view;
        v.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(v.getDrawingCache());
        return bitmap;
    }

    public void saveBitmap(Bitmap bitmap) {
        imagePath = new File(Environment.getExternalStorageDirectory() + "/screenshot.png");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }

    private void shareIt() {
        Uri imageUri = FileProvider.getUriForFile(
                getContext(),
                "com.tetravalstartups.dingdong.modules.passbook.provider",
                imagePath);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("image/*");
        String shareBody = "DingDong's Transaction Detail";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Coin Purchase");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }



    public interface TxnDetailsListener {
        void onButtonClicked(String text);
    }

}
