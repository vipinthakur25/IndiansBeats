package com.tetravalstartups.dingdong.modules.discover;

import android.content.Context;

import com.tetravalstartups.dingdong.api.APIClient;
import com.tetravalstartups.dingdong.api.CommonInterface;
import com.tetravalstartups.dingdong.auth.Master;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MostViewVideoPresenter {
    private Context context;
    private IMostViewVideo iMostViewVideo;


    public MostViewVideoPresenter(Context context, IMostViewVideo iMostViewVideo) {
        this.context = context;
        this.iMostViewVideo = iMostViewVideo;
    }

    public interface IMostViewVideo {
        void fetchResponse(MostViewVideo mostViewVideo);

        void fetchError(String error);
    }

    public void fetchViewVideo(int limit) {
        Master master = new Master(context);
        String userId = master.getId();
        CommonInterface commonInterface = APIClient.getRetrofitInstance().create(CommonInterface.class);
        Call<MostViewVideo> mostViewVideoCall = commonInterface.mostView(userId, limit);
        mostViewVideoCall.enqueue(new Callback<MostViewVideo>() {
            @Override
            public void onResponse(Call<MostViewVideo> call, Response<MostViewVideo> response) {
                if (response.code() == 200) {
                    iMostViewVideo.fetchResponse(response.body());
                } else if (response.code() == 400) {
                    iMostViewVideo.fetchError(response.message());
                } else {
                    iMostViewVideo.fetchError("Something went wrong...");
                }
            }

            @Override
            public void onFailure(Call<MostViewVideo> call, Throwable t) {
                iMostViewVideo.fetchError(t.getMessage());
            }
        });
    }
}
