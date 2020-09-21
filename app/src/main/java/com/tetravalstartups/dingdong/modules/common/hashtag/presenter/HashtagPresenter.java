package com.tetravalstartups.dingdong.modules.common.hashtag.presenter;

import android.content.Context;

import com.tetravalstartups.dingdong.api.APIClient;
import com.tetravalstartups.dingdong.api.CommonInterface;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.common.hashtag.model.TaggedVideos;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HashtagPresenter {
    private Context context;
    private IHashtag iHashtag;

    private CommonInterface commonInterface;

    public HashtagPresenter(Context context, IHashtag iHashtag) {
        this.context = context;
        this.iHashtag = iHashtag;
    }

    public interface IHashtag {
        void taggedVideosFetch(TaggedVideos taggedVideos);
        void taggedVideosNotFound(String message);
        void taggedVideosFetchFail(String error);
    }

    public void fetchVideos(String hashtag) {
        Master master = new Master(context);
        commonInterface = APIClient.getRetrofitInstance().create(CommonInterface.class);
        Call<TaggedVideos> call = commonInterface.fetchTaggedVideos(master.getId(), hashtag);
        call.enqueue(new Callback<TaggedVideos>() {
            @Override
            public void onResponse(Call<TaggedVideos> call, Response<TaggedVideos> response) {
                if (response.code() == 200) {
                    iHashtag.taggedVideosFetch(response.body());
                } else if (response.code() == 400) {
                    iHashtag.taggedVideosNotFound("No Videos");
                } else {
                    iHashtag.taggedVideosFetchFail(response.message());
                }
            }

            @Override
            public void onFailure(Call<TaggedVideos> call, Throwable t) {
                iHashtag.taggedVideosFetchFail(t.getMessage());
            }
        });
    }

}
