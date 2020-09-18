package com.tetravalstartups.dingdong.modules.discover;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.utils.EqualSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class DiscoverFragment extends Fragment implements DiscoverBannerPresenter.IDiscoverBanner {

    private View view;


    private ViewPager2 bannerPager;
    private Handler sliderHandler = new Handler();

    public DiscoverFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_discover, container, false);
        initView();
        return view;
    }

    private void initView() {

        RecyclerView recyclerSearch = view.findViewById(R.id.recyclerSearch);
        CardView contactsCardView = view.findViewById(R.id.contactsCardView);

        bannerPager = view.findViewById(R.id.bannerPager);

        recyclerSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerSearch.addItemDecoration(new EqualSpacingItemDecoration(0, EqualSpacingItemDecoration.VERTICAL));
        contactsCardView.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ContactsActivity.class);
            startActivity(intent);
        });

        fetchBanners();
        tendingNowRecyclerView();
        popularPeopleRecyclerView();
    }

    private void tendingNowRecyclerView() {
        RecyclerView trendingNowRecyclerView = view.findViewById(R.id.trendingNowRecyclerView);
        trendingNowRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        trendingNowRecyclerView.setHasFixedSize(true);

        List<TrendingNowModel> trendingNowModelArrayList = new ArrayList<>();
        trendingNowModelArrayList.add(new TrendingNowModel(R.drawable.new_image));
        trendingNowModelArrayList.add(new TrendingNowModel(R.drawable.new_image));
        trendingNowModelArrayList.add(new TrendingNowModel(R.drawable.new_image));
        trendingNowModelArrayList.add(new TrendingNowModel(R.drawable.new_image));
        trendingNowModelArrayList.add(new TrendingNowModel(R.drawable.new_image));
        trendingNowModelArrayList.add(new TrendingNowModel(R.drawable.new_image));
        trendingNowModelArrayList.add(new TrendingNowModel(R.drawable.new_image));

        TrendingNowAdapter trendingNowAdapter = new TrendingNowAdapter(getContext(), trendingNowModelArrayList);
        trendingNowRecyclerView.setAdapter(trendingNowAdapter);
    }

    private void popularPeopleRecyclerView() {
        RecyclerView popularPeopleRecyclerView = view.findViewById(R.id.popularPeopleRecyclerView);
        popularPeopleRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        popularPeopleRecyclerView.setHasFixedSize(true);

        List<PopularPeopleModel> popularPeopleModelList = new ArrayList<>();
        popularPeopleModelList.add(new PopularPeopleModel(R.drawable.emma, "Emma Watson", "123K Followers"));
        popularPeopleModelList.add(new PopularPeopleModel(R.drawable.emma, "Emma Watson", "123K Followers"));
        popularPeopleModelList.add(new PopularPeopleModel(R.drawable.emma, "Emma Watson", "123K Followers"));
        popularPeopleModelList.add(new PopularPeopleModel(R.drawable.emma, "Emma Watson", "123K Followers"));
        popularPeopleModelList.add(new PopularPeopleModel(R.drawable.emma, "Emma Watson", "123K Followers"));
        popularPeopleModelList.add(new PopularPeopleModel(R.drawable.emma, "Emma Watson", "123K Followers"));
        popularPeopleModelList.add(new PopularPeopleModel(R.drawable.emma, "Emma Watson", "123K Followers"));

        PopularPeopleAdapter popularPeopleAdapter = new PopularPeopleAdapter(getContext(), popularPeopleModelList);
        popularPeopleRecyclerView.setAdapter(popularPeopleAdapter);

    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            bannerPager.setCurrentItem(bannerPager.getCurrentItem() + 1);
        }
    };

    private void fetchBanners() {
        DiscoverBannerPresenter discoverBannerPresenter = new DiscoverBannerPresenter(getContext(), DiscoverFragment.this);
        discoverBannerPresenter.fetchBanner();

        bannerPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 3000);
    }

    @Override
    public void fetchBannerResponse(DiscoverBanner discoverBanner) {
        BannerPageAdapter bannerPageAdapter = new BannerPageAdapter(discoverBanner.getData(), bannerPager, getContext());
        bannerPageAdapter.notifyDataSetChanged();
        bannerPager.setAdapter(bannerPageAdapter);
    }

    @Override
    public void fetchError(String error) {

    }
}
