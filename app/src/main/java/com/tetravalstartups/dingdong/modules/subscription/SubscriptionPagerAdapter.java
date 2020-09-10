package com.tetravalstartups.dingdong.modules.subscription;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.tetravalstartups.dingdong.R;

import java.util.List;

public class SubscriptionPagerAdapter extends PagerAdapter {

    Context context;
    List<Subscription> subscriptionList;
    TextView tvName, tvAmount, tvValidity, tvBenefit, tvTotalBenefit, tvUploads;

    public SubscriptionPagerAdapter(Context context, List<Subscription> subscriptionList) {
        this.context = context;
        this.subscriptionList = subscriptionList;
    }

    @NonNull

    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.subscription_list_item, container, false);

        Subscription subscription = subscriptionList.get(position);

        tvName = layout.findViewById(R.id.tvName);
        tvAmount = layout.findViewById(R.id.tvAmount);
        tvValidity = layout.findViewById(R.id.tvValidity);
        tvBenefit = layout.findViewById(R.id.tvBenefit);
        tvTotalBenefit = layout.findViewById(R.id.tvTotalBenefit);
        tvUploads = layout.findViewById(R.id.tvUploads);

        tvName.setText("#"+subscription.getName());
        tvAmount.setText("₹"+subscription.getAmount());
        tvValidity.setText(subscription.getValidity()+" "+subscription.getValidity_unit());
        tvBenefit.setText("₹"+subscription.getBenefit()+" benefit every "+subscription.getBenefit_unit());
        tvTotalBenefit.setText("₹"+subscription.getTotal_benefit()+" total benefit");
        tvUploads.setText(subscription.getUploads()+" payable video per "+subscription.getUpload_unit());

        container.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return subscriptionList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
