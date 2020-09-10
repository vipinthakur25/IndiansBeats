package com.tetravalstartups.dingdong.modules.subscription;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.subscription.model.MySubscriptions;
import com.tetravalstartups.dingdong.modules.subscription.model.Plans;
import com.tetravalstartups.dingdong.utils.EqualSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SubscriptionActivity extends AppCompatActivity implements SubscriptionPresenter.ISubs, SubscribedPresenter.ISubscribed, CustomerRatingPresenter.IRating, View.OnClickListener {

    int page_position = 0;
    private RecyclerView recyclerSubs;
    private RecyclerView recyclerSubscribed;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private ViewPager viewPager;
    private TabLayout indicator;
    private List<CustomerRating> customerRatingList;
    private ImageView ivGoBack;
    private TextView tvNoSubs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        initView();
    }

    private void initView() {
        recyclerSubs = findViewById(R.id.recyclerSubs);
        recyclerSubscribed = findViewById(R.id.recyclerSubscribed);
        progressBar = findViewById(R.id.progressBar);

        firebaseAuth = FirebaseAuth.getInstance();
        String uid = firebaseAuth.getCurrentUser().getUid();

        viewPager = findViewById(R.id.viewPager);
        indicator = findViewById(R.id.indicator);

        ivGoBack = findViewById(R.id.ivGoBack);
        ivGoBack.setOnClickListener(this);

        tvNoSubs = findViewById(R.id.tvNoSubs);

        customerRatingList = new ArrayList<>();

        setupRecyclerSubs();

        setupRatingPager();

        setupRecyclerSubscribed();
    }

    private void setupRatingPager() {
        CustomerRatingPresenter customerRatingPresenter =
                new CustomerRatingPresenter(SubscriptionActivity.this,
                        SubscriptionActivity.this);
        customerRatingPresenter.fetchRating();
    }

    @Override
    public void fetchRatingSuccess(List<CustomerRating> customerRatingList) {
        CustomerReviewAdapter customerReviewAdapter =
                new CustomerReviewAdapter(SubscriptionActivity.this,
                        customerRatingList);
        customerReviewAdapter.notifyDataSetChanged();
        viewPager.setAdapter(customerReviewAdapter);

        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            public void run() {
                if (page_position == customerRatingList.size()) {
                    page_position = 0;
                } else {
                    page_position = page_position + 1;
                }
                viewPager.setCurrentItem(page_position, true);
                viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
                    @Override
                    public void transformPage(@NonNull View page, float position) {
                        page.setAlpha(0f);
                        page.setVisibility(View.VISIBLE);

                        // Start Animation for a short period of time
                        page.animate()
                                .alpha(1f)
                                .setDuration(1000);
                    }
                });
            }
        };

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 6000, 5000);

        indicator.setupWithViewPager(viewPager, true);
    }

    public void transformPage(View view, float position) {
        view.setTranslationX(view.getWidth() * -position);

        if(position <= -1.0F || position >= 1.0F) {
            view.setAlpha(0.0F);
        } else if( position == 0.0F ) {
            view.setAlpha(1.0F);
        } else {
            view.setAlpha(1.0F - Math.abs(position));
        }
    }

    @Override
    public void fetchRatingError(String error) {
        Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
    }

    private void setupRecyclerSubs() {
        recyclerSubs.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        SubscriptionPresenter subscriptionPresenter = new
                SubscriptionPresenter(SubscriptionActivity.this,
                SubscriptionActivity.this);

        subscriptionPresenter.fetchSubs();
    }

    public void setupRecyclerSubscribed() {
        recyclerSubscribed.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        SubscribedPresenter subscribedPresenter = new
                SubscribedPresenter(SubscriptionActivity.this,
                SubscriptionActivity.this);

        subscribedPresenter.fetchSubscribe();
    }

    @Override
    public void subsFetchSuccess(Plans plans) {
        SubscriptionAdapter subscriptionAdapter = new
                SubscriptionAdapter(SubscriptionActivity.this,
                plans.getPlanResponseList());
        subscriptionAdapter.notifyDataSetChanged();
        recyclerSubs.setAdapter(subscriptionAdapter);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void subsFetchError(String error) {
        Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void subscribeFetchSuccess(MySubscriptions mySubscriptions) {
        SubscribedAdapter subscribedAdapter =
                new SubscribedAdapter(SubscriptionActivity.this,
                        mySubscriptions.getData());
        subscribedAdapter.notifyDataSetChanged();
        recyclerSubscribed.setAdapter(subscribedAdapter);
        recyclerSubscribed.setVisibility(View.VISIBLE);
        tvNoSubs.setVisibility(View.GONE);
    }

    @Override
    public void subscribeFetchError(String error) {
        recyclerSubscribed.setVisibility(View.GONE);
        tvNoSubs.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (v == ivGoBack){
            onBackPressed();
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }
}


