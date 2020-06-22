package com.tetravalstartups.dingdong.modules.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.tetravalstartups.dingdong.MainActivity;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.home.video.Video;
import com.tetravalstartups.dingdong.modules.home.video.VideoAdapter;
import com.tetravalstartups.dingdong.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private View view;
    private ViewPager2 viewPager;
    private List<Video> videoList;
    private VideoAdapter videoAdapter;
    private FirebaseAuth auth;

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
        setUpVideoPager();
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null){
            ((MainActivity)getActivity()).getProfileData(auth.getCurrentUser().getUid());
        }
    }

    private void setUpVideoPager() {
        videoList = new ArrayList<>();

        final String[] uris = {"https://firebasestorage.googleapis.com/v0/b/dingdong-7billion.appspot.com/o/WhatsApp%20Video%202020-05-23%20at%2011.43.30%20AM.compressed.mp4?alt=media&token=6e55eefb-97e4-461f-9b08-dc056969e35e",
                "https://firebasestorage.googleapis.com/v0/b/dingdong-7billion.appspot.com/o/WhatsApp%20Video%202020-05-23%20at%2011.44.16%20AM.compressed.mp4?alt=media&token=48c6d1e1-f873-422a-a4db-e3f890d792b8",
                "https://firebasestorage.googleapis.com/v0/b/dingdong-7billion.appspot.com/o/WhatsApp%20Video%202020-05-23%20at%2011.45.23%20AM.compressed.mp4?alt=media&token=e83c0349-70d3-417b-b8b4-87e91c8766ab",
                "https://firebasestorage.googleapis.com/v0/b/dingdong-7billion.appspot.com/o/WhatsApp%20Video%202020-05-23%20at%2011.46.08%20AM.compressed.mp4?alt=media&token=9d53e6ee-ca4e-4378-9c49-f9cf4973514d",
                "https://firebasestorage.googleapis.com/v0/b/dingdong-7billion.appspot.com/o/WhatsApp%20Video%202020-05-23%20at%2011.47.35%20AM.compressed.mp4?alt=media&token=3e2c07ab-cf1b-4b0e-9972-d4a7f07f2fc7",
                "https://firebasestorage.googleapis.com/v0/b/dingdong-7billion.appspot.com/o/WhatsApp%20Video%202020-05-23%20at%2011.50.04%20AM.compressed.mp4?alt=media&token=25fa1352-28ee-4c01-8c1a-85a4f4882764"
        };

        videoList.add(new Video("1", Constants.VIDEO_1, "1", "user1", "", "1", "172", "1565", "27800"));
        videoList.add(new Video("2", Constants.VIDEO_2, "1", "user2", "", "15", "112", "415", "22200"));
        videoList.add(new Video("3", Constants.VIDEO_3, "1", "user3", "", "182", "12", "1565", "3200"));
        videoList.add(new Video("4", Constants.VIDEO_4, "1", "user4", "", "51", "1092", "1565", "2900"));
        videoList.add(new Video("5", Constants.VIDEO_5, "1", "user5", "", "19", "12", "1565", "2040"));
        videoList.add(new Video("6", Constants.VIDEO_6, "1", "user6", "", "71", "1542", "1875", "2000"));



        videoAdapter = new VideoAdapter(getContext(), videoList);
        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        viewPager.setAdapter(videoAdapter);
    }
}
