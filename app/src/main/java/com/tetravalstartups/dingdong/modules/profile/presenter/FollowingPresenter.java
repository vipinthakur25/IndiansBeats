package com.tetravalstartups.dingdong.modules.profile.presenter;

import android.content.Context;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.tetravalstartups.dingdong.api.APIClient;
import com.tetravalstartups.dingdong.api.RequestInterface;
import com.tetravalstartups.dingdong.modules.profile.model.following.Following;
import com.tetravalstartups.dingdong.modules.profile.model.following.FollowingResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowingPresenter {
    Context context;
    IFollowing iFollowing;
    private FirebaseFirestore db;
    private RequestInterface requestInterface;
    private static final String TAG = "FollowingPresenter";

    public FollowingPresenter(Context context, IFollowing iFollowing) {
        this.context = context;
        this.iFollowing = iFollowing;
    }

    public interface IFollowing {
        void followingFetchSuccess(List<FollowingResponse> followingsList);

        void followingFetchError(String error);
    }

    public void fetchFollowing(String user_id) {
        requestInterface = APIClient.getRetrofitInstance().create(RequestInterface.class);
        Call<Following> call = requestInterface.getUserFollowing(user_id);
        call.enqueue(new Callback<Following>() {
            @Override
            public void onResponse(Call<Following> call, Response<Following> response) {
                if (response.code() == 200) {
                    Following following = response.body();
                    List<FollowingResponse> followingList = new ArrayList<>(following.getData());
                    iFollowing.followingFetchSuccess(followingList);
                } else {
                    iFollowing.followingFetchError("No Followings");
                }

            }

            @Override
            public void onFailure(Call<Following> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                iFollowing.followingFetchError(t.getMessage());
            }
        });
    }

}
