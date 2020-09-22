package com.tetravalstartups.dingdong.modules.discover;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.utils.EqualSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class DiscoverFragment extends Fragment implements DiscoverBannerPresenter.IDiscoverBanner, MostViewVideoPresenter.IMostViewVideo, MostLikedVideoPresenter.IMostLikeVideo, View.OnClickListener {

    private View view;
    private ViewPager2 bannerPager;
    private Handler sliderHandler = new Handler();
    private RecyclerView mostViewVideoRecyclerView;
    private RecyclerView mostLikedVideoRecyclerView;
    private TextView tvMostLikedSeeMore;
    private TextView tvMostViewedSeeMore;
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

        tvMostLikedSeeMore = view.findViewById(R.id.tvMostLikedSeeMore);
        tvMostViewedSeeMore = view.findViewById(R.id.tvMostViewedSeeMore);
        bannerPager = view.findViewById(R.id.bannerPager);

        recyclerSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerSearch.addItemDecoration(new EqualSpacingItemDecoration(0, EqualSpacingItemDecoration.VERTICAL));


//        contactsCardView.setOnClickListener(view -> {
//            Intent intent = new Intent(getActivity(), ContactsActivity.class);
//            startActivity(intent);
//        });
        fetchBanners();

        fetchMostLikeVideo();

        fetchMostViewVideo();

        tendingNowRecycler();

        mostViewVideoRecyclerView();

        mostLikedVideoRecyclerView();

        popularPeopleRecyclerView();

        tvMostViewedSeeMore.setOnClickListener(this);
        tvMostLikedSeeMore.setOnClickListener(this);
    }

    private void fetchMostLikeVideo() {
        MostLikedVideoPresenter mostLikedVideoPresenter = new MostLikedVideoPresenter(getContext(), DiscoverFragment.this);
        mostLikedVideoPresenter.fetchLikeVideo(20);

    }

    private void mostLikedVideoRecyclerView() {
        mostLikedVideoRecyclerView = view.findViewById(R.id.mostLikedVideoRecyclerView);
        mostLikedVideoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mostLikedVideoRecyclerView.setHasFixedSize(true);
    }

    private void fetchMostViewVideo() {
        MostViewVideoPresenter mostViewVideoPresenter = new MostViewVideoPresenter(getContext(), DiscoverFragment.this);
        mostViewVideoPresenter.fetchViewVideo(20);
    }

    private void mostViewVideoRecyclerView() {
        mostViewVideoRecyclerView = view.findViewById(R.id.mostViewVideoRecyclerView);
        mostViewVideoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mostViewVideoRecyclerView.setHasFixedSize(true);
    }

    private void tendingNowRecycler() {
        RecyclerView trendingNowRecyclerView = view.findViewById(R.id.trendingNowRecyclerView);
        trendingNowRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        trendingNowRecyclerView.setHasFixedSize(true);

        List<TrendingNowModel> trendingNowModelArrayList = new ArrayList<>();
        trendingNowModelArrayList.add(new TrendingNowModel(R.drawable.dancing, "#dancing", "10K Videos"));
        trendingNowModelArrayList.add(new TrendingNowModel(R.drawable.singing, "#singing", "20K Videos"));
        trendingNowModelArrayList.add(new TrendingNowModel(R.drawable.sports, "#sport", "25k Videos"));
        trendingNowModelArrayList.add(new TrendingNowModel(R.drawable.technology, "#technology", "50K Videos"));
        trendingNowModelArrayList.add(new TrendingNowModel(R.drawable.comedy, "#comedy", "75K Videos"));
        trendingNowModelArrayList.add(new TrendingNowModel(R.drawable.covid, "#corona", "50K Videos"));

        TrendingNowAdapter trendingNowAdapter = new TrendingNowAdapter(getContext(), trendingNowModelArrayList);
        trendingNowRecyclerView.setAdapter(trendingNowAdapter);
    }

    private void popularPeopleRecyclerView() {
        RecyclerView popularPeopleRecyclerView = view.findViewById(R.id.popularPeopleRecyclerView);
        popularPeopleRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        popularPeopleRecyclerView.setHasFixedSize(true);

        List<PopularPeopleModel> popularPeopleModelList = new ArrayList<>();

        popularPeopleModelList.add(new PopularPeopleModel(R.drawable.salman, "786salmancool", "100 Followers", "Gou9Qd7QUkMQ4YBYuKqh3UZjrVk1"));
        popularPeopleModelList.add(new PopularPeopleModel(R.drawable.naman, "namanbhavsar69", "50 Followers", "S9uNFZ3zLYZSbmuufDWk69GYsSX2"));
        popularPeopleModelList.add(new PopularPeopleModel(R.drawable.sahil, "sahilsahu19999", "110 Followers", "xRYX1etmr3OzP62utfMWqDiscos2"));
        popularPeopleModelList.add(new PopularPeopleModel(R.drawable.kavitarathore, "Kavitarathor45", "220 Followers", "gAoweOAyo1MflhkujFg7uHrqchj1"));
        popularPeopleModelList.add(new PopularPeopleModel(R.drawable.yogikuber, "yogikuber", "450 Followers", "lUzboGGSONRVuspJ2CSl5AlZlcd2"));
        popularPeopleModelList.add(new PopularPeopleModel(R.drawable.alok, "alok6500", "100 Followers", "r7zAkoOggrT7Rl7zwCaa06QocKg2"));
        popularPeopleModelList.add(new PopularPeopleModel(R.drawable.raysahu, "raysahucsk", "50 Followers", "FsI5Cv01bBVHdBmknxXlH6000Bn1"));
        popularPeopleModelList.add(new PopularPeopleModel(R.drawable.singhrichu, "singhrichu052", "185 Followers", "oBcxf6TaDOQrHh1QgWzCGMFQbSK2"));
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
    public void fetchResponse(MostViewVideo mostViewVideo) {
        MostViewVideoAdapter mostViewVideoAdapter = new MostViewVideoAdapter(getContext(), mostViewVideo.getData());
        mostViewVideoAdapter.notifyDataSetChanged();
        mostViewVideoRecyclerView.setAdapter(mostViewVideoAdapter);

    }

    @Override
    public void fetchResponse(MostLikedVideo mostLikedVideo) {
        MostLikedVideoAdapter mostLikedVideoAdapter = new MostLikedVideoAdapter(getContext(), mostLikedVideo.getData());
        mostLikedVideoAdapter.notifyDataSetChanged();
        mostLikedVideoRecyclerView.setAdapter(mostLikedVideoAdapter);
    }

    @Override
    public void fetchError(String error) {
        Toast.makeText(getContext(), "" + error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        if (view == tvMostViewedSeeMore){
           Intent intent = new Intent(getActivity(), MostViewedActivity.class);
           getContext().startActivity(intent);
        }
        if (view == tvMostLikedSeeMore){
            Intent intent = new Intent(getActivity(), MostLikedActivity.class);
            getContext().startActivity(intent);
        }
    }
}
