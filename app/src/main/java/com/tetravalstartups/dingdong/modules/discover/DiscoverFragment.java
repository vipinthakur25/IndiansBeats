package com.tetravalstartups.dingdong.modules.discover;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.tetravalstartups.dingdong.R;

import java.util.ArrayList;
import java.util.List;

public class DiscoverFragment extends Fragment implements DiscoverBannerMainPresenter.IDiscoverMainBanner {

    private View view;
    private ImageSlider imageSlider;

    public DiscoverFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_discover, container, false);
        initView();
        return view;
    }

    private void initView() {
        imageSlider = view.findViewById(R.id.imageSlider);
        fetchBanners();
    }

    private void fetchBanners() {
        DiscoverBannerMainPresenter discoverBannerMainPresenter =
                new DiscoverBannerMainPresenter(getContext(),
                        DiscoverFragment.this);
        discoverBannerMainPresenter.fetchBanner();
    }


    @Override
    public void fetchBannerSuccess(ArrayList<SlideModel> discoverBannerMainList) {
        imageSlider.setImageList(discoverBannerMainList, ScaleTypes.FIT);
    }

    @Override
    public void fetchBannerError(String error) {
        Toast.makeText(getContext(), ""+error, Toast.LENGTH_SHORT).show();
    }
}
