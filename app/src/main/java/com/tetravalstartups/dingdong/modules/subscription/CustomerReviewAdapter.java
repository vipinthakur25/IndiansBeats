package com.tetravalstartups.dingdong.modules.subscription;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.tetravalstartups.dingdong.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerReviewAdapter extends PagerAdapter {

    Context context;
    List<CustomerRating> customerRatingList;

    public CustomerReviewAdapter(Context context, List<CustomerRating> customerRatingList) {
        this.context = context;
        this.customerRatingList = customerRatingList;
    }

    @Override
    public int getCount() {
        return customerRatingList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.customer_reviews_list_item, null);

        CustomerRating customerRating = customerRatingList.get(position);

        LinearLayout lvRating = view.findViewById(R.id.lvRating);
        ImageView ivPhoto = view.findViewById(R.id.ivPhoto);
        RatingBar barRating = view.findViewById(R.id.barRating);
        TextView tvReview = view.findViewById(R.id.tvReview);
        TextView tvName = view.findViewById(R.id.tvName);

        Glide.with(context).load(customerRating.getPhoto()).placeholder(R.drawable.dingdong_placeholder).into(ivPhoto);
        barRating.setRating(customerRating.getRating());
        tvReview.setText(customerRating.getReview());
        tvName.setText(customerRating.getName());

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }

}
