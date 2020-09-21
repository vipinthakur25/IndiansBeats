package com.tetravalstartups.dingdong.modules.notification.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.notification.model.Notification;
import com.tetravalstartups.dingdong.modules.notification.presenter.NotificationPresenter;
import com.tetravalstartups.dingdong.modules.notification.view.adapter.NotificationAdapter;

import java.util.ArrayList;

public class NotificationFragment extends Fragment implements NotificationPresenter.INotify {

    private View view;
    private RecyclerView recyclerNotification;
    private LinearLayout lvLoadingData;
    private LinearLayout lvNoData;

    public NotificationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notification, container, false);
        initView();
        return view;
    }

    private void initView() {
        recyclerNotification = view.findViewById(R.id.recyclerNotification);
        lvLoadingData = view.findViewById(R.id.lvLoadingData);
        lvNoData = view.findViewById(R.id.lvNoData);
        lvLoadingData.setVisibility(View.VISIBLE);
        setupNotificationPresenter();
    }

    private void setupNotificationPresenter() {
        NotificationPresenter notificationPresenter = new NotificationPresenter(getContext(), NotificationFragment.this);
        notificationPresenter.fetchNotification();
    }

    @Override
    public void notificationsNotFound(String message) {
        recyclerNotification.setVisibility(View.GONE);
        lvLoadingData.setVisibility(View.GONE);
        lvNoData.setVisibility(View.VISIBLE);
    }

    @Override
    public void notificationsFound(ArrayList<Notification> notificationArrayList) {
        recyclerNotification.setLayoutManager(new LinearLayoutManager(getContext()));
        NotificationAdapter notificationAdapter = new NotificationAdapter(getContext(), notificationArrayList);
        notificationAdapter.notifyDataSetChanged();
        recyclerNotification.setAdapter(notificationAdapter);
        lvLoadingData.setVisibility(View.GONE);
        lvNoData.setVisibility(View.GONE);
        recyclerNotification.setVisibility(View.VISIBLE);
    }
}
