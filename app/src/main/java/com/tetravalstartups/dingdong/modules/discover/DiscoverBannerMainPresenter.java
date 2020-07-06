package com.tetravalstartups.dingdong.modules.discover;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.tetravalstartups.dingdong.R;

import java.util.ArrayList;
import java.util.List;

public class DiscoverBannerMainPresenter {

    Context context;
    IDiscoverMainBanner iDiscoverMainBanner;
    FirebaseFirestore db;

    public DiscoverBannerMainPresenter(Context context, IDiscoverMainBanner iDiscoverMainBanner) {
        this.context = context;
        this.iDiscoverMainBanner = iDiscoverMainBanner;
    }

    public interface IDiscoverMainBanner {
        void fetchBannerSuccess(ArrayList<SlideModel> discoverBannerMainList);

        void fetchBannerError(String error);
    }

    public void fetchBanner() {
        ArrayList<SlideModel> discoverBannerMainList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        db.collection("discover_banner")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (documentSnapshots.getDocuments().isEmpty()){
                            iDiscoverMainBanner.fetchBannerError("No Banners");
                        } else {
                            discoverBannerMainList.clear();
                            for (DocumentSnapshot snapshot : documentSnapshots.getDocuments()){
                               SlideModel slideModel = new SlideModel(snapshot.getString("banner_url"), ScaleTypes.FIT);
                               discoverBannerMainList.add(slideModel);
                            }
                            iDiscoverMainBanner.fetchBannerSuccess(discoverBannerMainList);
                        }
                    }
                });
    }


}
