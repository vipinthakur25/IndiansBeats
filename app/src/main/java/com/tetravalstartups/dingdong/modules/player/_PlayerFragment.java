package com.tetravalstartups.dingdong.modules.player;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.krishna.fileloader.FileLoader;
import com.krishna.fileloader.listener.FileRequestListener;
import com.krishna.fileloader.pojo.FileResponse;
import com.krishna.fileloader.request.FileLoadRequest;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.home.video.Video;
import com.tetravalstartups.dingdong.utils.DDLoading;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class _PlayerFragment extends Fragment implements View.OnClickListener {

    private View view;
    private RecyclerView recyclerVideos;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    public int lastPosition = -1;
    private List<Video> videoList = new ArrayList<>();
    private TextView tvTrending, tvFollowing;
    private ImageView ivWhatsapp;
    private DDLoading ddLoading;
    private PlayerViewHolder viewHolder;

    public _PlayerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_player, container, false);
        initView();
        return view;
    }

    private void initView() {
        recyclerVideos = view.findViewById(R.id.recyclerVideos);
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        tvFollowing = view.findViewById(R.id.tvFollowing);
        tvTrending = view.findViewById(R.id.tvTrending);

        ivWhatsapp = view.findViewById(R.id.ivWhatsapp);
        YoYo.with(Techniques.Pulse)
                .duration(2500)
                .repeat(YoYo.INFINITE)
                .playOn(ivWhatsapp);

        ddLoading = DDLoading.getInstance();

        ivWhatsapp.setOnClickListener(this);

        tvFollowing.setOnClickListener(this);
        tvTrending.setOnClickListener(this);

        setupAdapter();
    }

    private void setupAdapter() {
//        double randomDouble = Math.random();
//        randomDouble = randomDouble * 1000 + 1;
//        int randomInt = (int) randomDouble;
//        CollectionReference videoRef = mFirestore.collection("videos");
//        Query query = videoRef.whereEqualTo("video_status", Constants.VIDEO_STATUS_PUBLIC)
//                .whereLessThan("video_index", randomInt+"");
//        PagedList.Config config = new PagedList.Config.Builder()
//                .setEnablePlaceholders(false)
//                .setPrefetchDistance(5)
//                .setPageSize(10)
//                .build();
//
//        FirestorePagingOptions<Video> options = new FirestorePagingOptions.Builder<Video>()
//                .setLifecycleOwner(this)
//                .setQuery(query, config, Video.class)
//                .build();


//        FirestorePagingAdapter<Video, PlayerViewHolder> mAdapter = new FirestorePagingAdapter<Video, PlayerViewHolder>(options) {
//
//            @NonNull
//            @Override
//            public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = getLayoutInflater().inflate(R.layout.video_list_item, parent, false);
//                return new PlayerViewHolder(view);
//            }
//
//            @Override
//            protected void onBindViewHolder(@NonNull PlayerViewHolder holder, int position, @NonNull Video model) {
//                holder.playVideo(model);
//                holder.cdSpinAnimation();
//            }
//
//            @Override
//            public void onViewAttachedToWindow(@NonNull PlayerViewHolder holder) {
//                super.onViewAttachedToWindow(holder);
//                holder.cdSpinAnimation();
//            }
//
//            @Override
//            public void onViewDetachedFromWindow(@NonNull PlayerViewHolder holder) {
//
//            }
//
//            @Override
//            protected void onError(@NonNull Exception e) {
//                super.onError(e);
//                Log.e("MainActivity", Objects.requireNonNull(e.getMessage()));
//            }
//
//            @Override
//            protected void onLoadingStateChanged(@NonNull LoadingState state) {
//                switch (state) {
//                    case LOADING_INITIAL:
//                    case LOADING_MORE:
//                        mSwipeRefreshLayout.setRefreshing(true);
//                        break;
//
//                    case LOADED:
//
//                    case FINISHED:
//                        mSwipeRefreshLayout.setRefreshing(false);
//                        break;
//
//                    case ERROR:
//                        Toast.makeText(
//                                getContext(),
//                                "Error Occurred!",
//                                Toast.LENGTH_SHORT
//                        ).show();
//
//                        mSwipeRefreshLayout.setRefreshing(false);
//                        break;
//                }
//            }
//
//        };

        SnapHelper snapHelper = new PagerSnapHelper();
        recyclerVideos.setLayoutManager(new LinearLayoutManager(getContext()));
        if (recyclerVideos.getOnFlingListener() == null)
            snapHelper.attachToRecyclerView(recyclerVideos);
        //recyclerVideos.setAdapter(mAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        if (v == tvTrending) {
            tvTrending.setTextColor(getResources().getColor(R.color.colorBrandYellow));
            tvFollowing.setTextColor(getResources().getColor(R.color.colorTextTitle));
            setupAdapter();
        }

        if (v == tvFollowing) {
            Toast.makeText(getContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
//            tvTrending.setTextColor(getResources().getColor(R.color.colorTextTitle));
//            tvFollowing.setTextColor(getResources().getColor(R.color.colorBrandYellow));
//            setupAdapter();
        }

        if (v == ivWhatsapp){
            ddLoading.showProgress(getContext(), "Loading...", false);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("banners").document("share_banner")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            String url = task.getResult().getString("url");
                            FileLoader.with(getContext())
                                    .load("https://firebasestorage.googleapis.com/v0/b/dingdong-7billion.appspot.com/o/banners%2Fdd_2_0_1.png?alt=media&token=e62d4614-cccb-4888-b1a1-9709a203689c",false)
                                    .fromDirectory("test4", FileLoader.DIR_INTERNAL)
                                    .asFile(new FileRequestListener<File>() {
                                        @Override
                                        public void onLoad(FileLoadRequest request, FileResponse<File> response) {
                                            File loadedFile = response.getBody();
                                            Uri uri = Uri.parse(loadedFile.getAbsolutePath());
                                            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                                            whatsappIntent.setType("text/plain");
                                            whatsappIntent.setPackage("com.whatsapp.w4b");
                                            whatsappIntent.putExtra(Intent.EXTRA_TEXT, "The text you wanted to share");
                                            whatsappIntent.putExtra(Intent.EXTRA_STREAM, uri);
                                            whatsappIntent.setType("image/jpeg");
                                            whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                                            try {
                                                startActivity(whatsappIntent);
                                            } catch (android.content.ActivityNotFoundException ex) {
                                                Toast.makeText(getContext(), "not installed", Toast.LENGTH_SHORT).show();
                                            }
//                                            try {
//                                                Intent shareIntent = new Intent();
//                                                shareIntent.setAction(Intent.ACTION_SEND);
//                                                shareIntent.setPackage("com.whatsapp");
//                                               // shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey your friend is using *Ding Dong* which is an *Hybrid video sharing app*. Here is the *download* link:\nhttps://bit.ly/3fSqiQq\n\n*Create . Share . Earn*");
//                                                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(loadedFile.getPath()));
//                                                shareIntent.setType("image/*");
//                                                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                                                startActivity(shareIntent);
//                                                ddLoading.hideProgress();
//                                            } catch (Exception e) {
//                                                e.printStackTrace();
//                                                Intent shareIntent = new Intent();
//                                                shareIntent.setAction(Intent.ACTION_SEND);
//                                                shareIntent.setPackage("com.whatsapp.w4b");
//                                           //     shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey your friend is using *Ding Dong* which is an *Hybrid video sharing app*. Here is the *download* link:\nhttps://bit.ly/3fSqiQq\n\n*Create . Share . Earn*");
//                                                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(loadedFile.getPath()));
//                                                shareIntent.setType("image/*");
//                                                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                                                startActivity(shareIntent);
//                                                ddLoading.hideProgress();
//                                            }
                                        }

                                        @Override
                                        public void onError(FileLoadRequest request, Throwable t) {
                                        }
                                    });
                        }
                    });
        }
    }
}

