package com.tetravalstartups.dingdong.modules.profile.presenter;

import android.content.Context;
import android.util.Log;

import com.tetravalstartups.dingdong.api.APIClient;
import com.tetravalstartups.dingdong.api.AuthInterface;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.passbook.TransactionPresenter;
import com.tetravalstartups.dingdong.modules.profile.model.HelpRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HelpRequestPresenter {
    private Context context;
    private IHelpRequest iHelpRequest ;

    private static final String TAG = "HelpRequestPresenter";


    public HelpRequestPresenter(Context context, IHelpRequest iHelpRequest) {
        this.context = context;
        this.iHelpRequest = iHelpRequest;
    }

    public interface IHelpRequest{
        void fetchResponse(HelpRequest helpRequest);

        void fetchError(String error);
    }

    public void fetchResponse(){
        Master master = new Master(context);
        String userId = master.getId();
        AuthInterface authInterface = APIClient.getRetrofitInstance().create(AuthInterface.class);
        Call<HelpRequest> helpRequestCall = authInterface.helpRequest(userId);
        helpRequestCall.enqueue(new Callback<HelpRequest>() {
            @Override
            public void onResponse(Call<HelpRequest> call, Response<HelpRequest> response) {
                //Log.e(TAG, "onResponse: "+response.message());
                if (response.code() == 200){
                    iHelpRequest.fetchResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<HelpRequest> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage());
                    iHelpRequest.fetchError(t.getMessage());
            }
        });
    }
}
