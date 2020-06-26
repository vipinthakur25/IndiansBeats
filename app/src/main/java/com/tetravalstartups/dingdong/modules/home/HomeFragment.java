package com.tetravalstartups.dingdong.modules.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cloudinary.Transformation;
import com.cloudinary.android.MediaManager;
import com.cloudinary.transformation.Layer;
import com.google.firebase.auth.FirebaseAuth;
import com.tetravalstartups.dingdong.MainActivity;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.home.video.Video;
import com.tetravalstartups.dingdong.modules.home.video.VideoAdapter;
import com.tetravalstartups.dingdong.modules.home.video.VideoPresenter;
import com.tetravalstartups.dingdong.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements VideoPresenter.IVideo {

    private View view;
    private ViewPager2 viewPager;
    private VideoAdapter videoAdapter;
    private FirebaseAuth auth;
    private VideoPresenter videoPresenter;

    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        initView();
        return view;
    }

    private void initView() {
        viewPager = view.findViewById(R.id.viewPager);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null){
            ((MainActivity)getActivity()).getProfileData(auth.getCurrentUser().getUid());
        }

        videoPresenter = new VideoPresenter(getContext(), HomeFragment.this);
        //videoPresenter.fetchVideos();
    }

    @Override
    public void fetchVideosSuccess(List<Video> videoList) {
        videoAdapter = new VideoAdapter(getContext(), videoList);
        videoAdapter.notifyDataSetChanged();
        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        viewPager.setAdapter(videoAdapter);
    }

    @Override
    public void fetchVideosError(String error) {
        Toast.makeText(getContext(), ""+error, Toast.LENGTH_SHORT).show();
    }
}
