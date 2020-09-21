package com.tetravalstartups.dingdong.modules.discover;

import android.content.Context;

import com.tetravalstartups.dingdong.api.APIClient;
import com.tetravalstartups.dingdong.api.CommonInterface;
import com.tetravalstartups.dingdong.auth.Master;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MostLikedVideoPresenter {
    private Context context;
    private IMostLikeVideo iMostLikeVideo;


    public MostLikedVideoPresenter(Context context, IMostLikeVideo iMostLikeVideo) {
        this.context = context;
        this.iMostLikeVideo = iMostLikeVideo;
    }

    public interface IMostLikeVideo{
        void fetchResponse(MostLikedVideo mostLikedVideo);

        void fetchError(String error);
    }
    public void fetchLikeVideo(){
        Master master = new Master(context);
        String userId = master.getId();

        CommonInterface commonInterface = APIClient.getRetrofitInstance().create(CommonInterface.class);
        Call<MostLikedVideo> mostLikedVideoCall = commonInterface.mostLiked(userId, 20);

        mostLikedVideoCall.enqueue(new Callback<MostLikedVideo>() {
            @Override
            public void onResponse(Call<MostLikedVideo> call, Response<MostLikedVideo> response) {
                if (response.code() == 200){
                    iMostLikeVideo.fetchResponse(response.body());
                } else if (response.code() == 400){
                    iMostLikeVideo.fetchError(response.message());
                }else {
                    iMostLikeVideo.fetchError("Something went wrong...");
                }
            }

            @Override
            public void onFailure(Call<MostLikedVideo> call, Throwable t) {
                iMostLikeVideo.fetchError(t.getMessage());
            }
        });
    }
}
