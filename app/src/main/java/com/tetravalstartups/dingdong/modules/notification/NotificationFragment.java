package com.tetravalstartups.dingdong.modules.notification;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tetravalstartups.dingdong.R;

import java.util.regex.Pattern;

public class NotificationFragment extends Fragment {

    private View view;

    public NotificationFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_message, container, false);
        initView();
        return view;
    }

    private void initView() {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
    }
}
