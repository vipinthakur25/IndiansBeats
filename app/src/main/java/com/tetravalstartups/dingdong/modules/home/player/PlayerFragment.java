package com.tetravalstartups.dingdong.modules.home.player;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cloudinary.Transformation;
import com.cloudinary.android.MediaManager;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.tetravalstartups.dingdong.App;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.home.video.Video;
import com.tetravalstartups.dingdong.utils.Constants;

import static com.tetravalstartups.dingdong.App.simpleCache;

public class PlayerFragment extends Fragment implements Player.EventListener {

    private View view;
    private RecyclerView recyclerPlayer;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SimpleCache simpleCache;
    private CacheDataSourceFactory cacheDataSourceFactory;
    private FirestorePagingAdapter<Video, PlayerViewHolder> mAdapter;
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private CollectionReference mPostsCollection = mFirestore.collection("videos");
    private Query mQuery = mPostsCollection.whereEqualTo("video_status", Constants.VIDEO_STATUS_PUBLIC);
    private SimpleExoPlayer player;
    private Video onVideo;
    private PlayerViewHolder playerVH;

    public int lastPosition = -1;

    private final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

    public PlayerFragment() {
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
        recyclerPlayer = view.findViewById(R.id.recyclerPlayer);
        recyclerPlayer.setHasFixedSize(true);
        recyclerPlayer.setLayoutManager(layoutManager);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        setupAdapter();

        recyclerPlayer.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int position = layoutManager.findFirstCompletelyVisibleItemPosition();
                    if (lastPosition != position) {
                        if (position % 10 != 6) {
                            //Animation animation = AnimationUtils.loadAnimation(getRoot().getContext(), R.anim.slow_rotate);
                            if (recyclerPlayer.getLayoutManager() != null) {
                                int index = position - ((position + 4) / 10);
                                View view = recyclerPlayer.getLayoutManager().findViewByPosition(position);
                                if (view != null) {
                                    lastPosition = position;
                                    String url = MediaManager.get().url().transformation(new Transformation().quality(5)).resourceType("video").generate("user_uploaded_videos/"+onVideo.getId()+".webm");
                                    playVideo(url, playerVH);
                                   // ItemVideoListBinding binding1 = DataBindingUtil.bind(view);
//                                    if (binding1 != null) {
//                                        //binding1.imgSound.startAnimation(animation);
//                                        //new GlobalApi().increaseView(binding1.getModel().getPostId());
//
//                                    }
                                }
                            }
                        } else {
                            if (player != null) {
                                player.setPlayWhenReady(false);
                                player.stop();
                                player.release();
                                player = null;
                                lastPosition = position;
                            }
                        }
                    }
                }

            }
        });

    }

    private void setupAdapter() {

        // Init Paging Configuration
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(2)
                .setPageSize(10)
                .build();

        // Init Adapter Configuration
        FirestorePagingOptions options = new FirestorePagingOptions.Builder<Video>()
                .setLifecycleOwner(this)
                .setQuery(mQuery, config, Video.class)
                .build();

        // Instantiate Paging Adapter
        mAdapter = new FirestorePagingAdapter<Video, PlayerViewHolder>(options) {
            @NonNull
            @Override
            public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = getLayoutInflater().inflate(R.layout.auto_video_list_item_layout, parent, false);
                return new PlayerViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull PlayerViewHolder holder, int position, @NonNull Video model) {
                onVideo = model;
                playerVH = holder;
            }

            @Override
            protected void onError(@NonNull Exception e) {
                super.onError(e);
                Log.e("MainActivity", e.getMessage());
            }

            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                switch (state) {
                    case LOADING_INITIAL:
                    case LOADING_MORE:
                        swipeRefreshLayout.setRefreshing(true);
                        break;

                    case LOADED:
                        swipeRefreshLayout.setRefreshing(false);
                        break;

                    case ERROR:
                        Toast.makeText(
                                getContext(),
                                "Error Occurred!",
                                Toast.LENGTH_SHORT
                        ).show();

                        swipeRefreshLayout.setRefreshing(false);
                        break;

                    case FINISHED:
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }

        };

        recyclerPlayer.setAdapter(mAdapter);

    }

    private void playVideo(String videoUrl, PlayerViewHolder playerViewHolder) {
        Toast.makeText(getContext(), ""+videoUrl, Toast.LENGTH_SHORT).show();

        if (player != null) {
            player.removeListener(this);
            player.setPlayWhenReady(false);
            player.release();
        }
        if (getActivity() != null) {
            player = new SimpleExoPlayer.Builder(getActivity()).build();
            simpleCache = App.simpleCache;
            cacheDataSourceFactory = new CacheDataSourceFactory(simpleCache, new DefaultHttpDataSourceFactory(Util.getUserAgent(getActivity(), "dingdong"))
                    , CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR);

            ProgressiveMediaSource progressiveMediaSource = new ProgressiveMediaSource.Factory(cacheDataSourceFactory).createMediaSource(Uri.parse(videoUrl));
            playerViewHolder.playerView.setPlayer(player);
            player.setPlayWhenReady(true);
            player.seekTo(0, 0);
            player.setRepeatMode(Player.REPEAT_MODE_ALL);
            player.addListener(this);
            playerViewHolder.playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
            player.prepare(progressiveMediaSource, true, false);
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    public class PlayerViewHolder extends RecyclerView.ViewHolder {

        private PlayerView playerView;

        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            playerView = itemView.findViewById(R.id.playerView);
        }

    }

}