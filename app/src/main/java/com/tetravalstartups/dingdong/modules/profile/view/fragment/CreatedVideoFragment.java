package com.tetravalstartups.dingdong.modules.profile.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.profile.model.InProfileCreatedVideo;
import com.tetravalstartups.dingdong.modules.profile.view.adapter.InProfileCreatedVideoAdapter;
import com.tetravalstartups.dingdong.utils.Constants;
import com.tetravalstartups.dingdong.utils.EqualSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class CreatedVideoFragment extends Fragment {

    private View view;
    private RecyclerView recyclerCreatedVideo;
    private List<InProfileCreatedVideo> inProfileCreatedVideoList;
    private InProfileCreatedVideoAdapter inProfileCreatedVideoAdapter;

    public CreatedVideoFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_created_video, container, false);
        initView();
        return view;
    }

    private void initView() {
        recyclerCreatedVideo = view.findViewById(R.id.recyclerCreatedVideo);
        fetchCreatedVideos();
    }

    private void fetchCreatedVideos() {
        inProfileCreatedVideoList = new ArrayList<>();
        recyclerCreatedVideo.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerCreatedVideo.addItemDecoration(new EqualSpacingItemDecoration(4, EqualSpacingItemDecoration.GRID));
        inProfileCreatedVideoList.add(new InProfileCreatedVideo("1", "10K", Constants.VIDEO_1));
        inProfileCreatedVideoList.add(new InProfileCreatedVideo("2", "2K", Constants.VIDEO_2));
        inProfileCreatedVideoList.add(new InProfileCreatedVideo("3", "16K", Constants.VIDEO_3));
        inProfileCreatedVideoList.add(new InProfileCreatedVideo("4", "485", Constants.VIDEO_4));
        inProfileCreatedVideoList.add(new InProfileCreatedVideo("5", "95K", Constants.VIDEO_5));
        inProfileCreatedVideoList.add(new InProfileCreatedVideo("6", "100K", Constants.VIDEO_6));
        inProfileCreatedVideoAdapter = new InProfileCreatedVideoAdapter(getContext(), inProfileCreatedVideoList);
        inProfileCreatedVideoAdapter.notifyDataSetChanged();
        recyclerCreatedVideo.setAdapter(inProfileCreatedVideoAdapter);
    }
}
