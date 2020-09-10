package com.tetravalstartups.dingdong.modules.profile.presenter;

import android.content.Context;

import com.tetravalstartups.dingdong.api.APIClient;
import com.tetravalstartups.dingdong.api.RequestInterface;
import com.tetravalstartups.dingdong.modules.profile.model.followers.Followers;
import com.tetravalstartups.dingdong.modules.profile.model.followers.FollowersResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowerPresenter {
    Context context;
    IFollower iFollower;
    private RequestInterface requestInterface;

    public FollowerPresenter(Context context, IFollower iFollower) {
        this.context = context;
        this.iFollower = iFollower;
    }

    public interface IFollower {
        void followerFetchSuccess(List<FollowersResponse> followersList);

        void followerFetchError(String error);
    }

    public void fetchFollowers(String user_id){
        requestInterface = APIClient.getRetrofitInstance().create(RequestInterface.class);
        Call<Followers> call = requestInterface.getUserFollowers(user_id);
        call.enqueue(new Callback<Followers>() {
            @Override
            public void onResponse(Call<Followers> call, Response<Followers> response) {
                if (response.code() == 200) {
                    Followers followers = response.body();
                    List<FollowersResponse> followersResponseList = new ArrayList<>(followers.getData());
                    iFollower.followerFetchSuccess(followersResponseList);
                } else {
                    iFollower.followerFetchError("No Followers...");
                }
            }

            @Override
            public void onFailure(Call<Followers> call, Throwable t) {
                iFollower.followerFetchError(t.getMessage());
            }
        });
    }

}
