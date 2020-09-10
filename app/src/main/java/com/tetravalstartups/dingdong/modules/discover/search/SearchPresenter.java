package com.tetravalstartups.dingdong.modules.discover.search;

import android.content.Context;
import android.util.Log;

import com.tetravalstartups.dingdong.api.APIClient;
import com.tetravalstartups.dingdong.api.RequestInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPresenter {
    private Context context;
    private ISearch iSearch;
    private RequestInterface requestInterface;
    private static final String TAG = "SearchPresenter";

    public SearchPresenter(Context context, ISearch iSearch) {
        this.context = context;
        this.iSearch = iSearch;
    }

    public interface ISearch {
        void searchSuccess(List<SearchResponse> searchResponseList);
        void searchFailed(String error);
    }

    public void fetchSearchQuery(String user_id, int last_index, String query) {
        requestInterface = APIClient.getRetrofitInstance().create(RequestInterface.class);
        Call<Search> call = requestInterface.searchUsers(user_id, last_index, query);
        call.enqueue(new Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response) {
                if (response.code() == 200) {
                    Search search = response.body();
                    List<SearchResponse> searchResponseList = new ArrayList<>(search.getSearchResponses());
                    iSearch.searchSuccess(searchResponseList);
                } else {
                    iSearch.searchFailed("No Users");
                }
            }

            @Override
            public void onFailure(Call<Search> call, Throwable t) {
                iSearch.searchFailed(t.getMessage());
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
    }
}
