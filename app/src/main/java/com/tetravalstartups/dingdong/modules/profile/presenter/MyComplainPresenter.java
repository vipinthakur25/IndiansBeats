package com.tetravalstartups.dingdong.modules.profile.presenter;

import android.content.Context;
import android.util.Log;

import com.tetravalstartups.dingdong.api.APIClient;
import com.tetravalstartups.dingdong.api.AuthInterface;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.profile.model.MyComplain;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyComplainPresenter {
    private Context context;
    private IComplainRequest iComplain;
    private static final String TAG = "MyComplainPresenter";

    public MyComplainPresenter(Context context, IComplainRequest iComplain) {
        this.context = context;
        this.iComplain = iComplain;
    }
    public interface IComplainRequest{
        void fetchResponse(MyComplain myComplain);

        void fetchError(String error);
    }
    public void fetchComplain(){
        Master master = new Master(context);
        String userId = master.getId();

        AuthInterface authInterface = APIClient.getRetrofitInstance().create(AuthInterface.class);
        Call<MyComplain> myComplainCall = authInterface.complainRequest(userId);
        myComplainCall.enqueue(new Callback<MyComplain>() {
            @Override
            public void onResponse(@NotNull Call<MyComplain> call, @NotNull Response<MyComplain> response) {
              Log.e(TAG, "onResponse: "+response.message());
                if (response.code() == 200){
                    iComplain.fetchResponse(response.body());
                }
            }

            @Override
            public void onFailure(@NotNull Call<MyComplain> call, @NotNull Throwable t) {
                iComplain.fetchError(t.getMessage());
            }
        });
    }
}
