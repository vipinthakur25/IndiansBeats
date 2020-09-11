package com.tetravalstartups.dingdong.modules.discover;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.auth.FirebaseAuth;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.discover.search.SearchAdapter;
import com.tetravalstartups.dingdong.modules.discover.search.SearchPresenter;
import com.tetravalstartups.dingdong.modules.discover.search.SearchResponse;
import com.tetravalstartups.dingdong.utils.EqualSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class DiscoverFragment extends Fragment implements DiscoverBannerMainPresenter.IDiscoverMainBanner, TextWatcher, SearchPresenter.ISearch {

    private View view;
    private ImageSlider imageSlider;
    private EditText etSearch;
    private FrameLayout frameUsers;
    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerSearch;
    private TextView tvNoData;

    public DiscoverFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_discover, container, false);
        initView();
        return view;
    }

    private void initView() {
        frameUsers  = view.findViewById(R.id.frameUsers);
       // imageSlider = view.findViewById(R.id.imageSlider);
        etSearch = view.findViewById(R.id.etSearch);
        recyclerSearch = view.findViewById(R.id.recyclerSearch);
        tvNoData = view.findViewById(R.id.tvNoData);

        etSearch.addTextChangedListener(this);
        recyclerSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerSearch.addItemDecoration(new EqualSpacingItemDecoration(0, EqualSpacingItemDecoration.VERTICAL));

        firebaseAuth = FirebaseAuth.getInstance();
        fetchBanners();
    }

    private void fetchBanners() {
        DiscoverBannerMainPresenter discoverBannerMainPresenter =
                new DiscoverBannerMainPresenter(getContext(),
                        DiscoverFragment.this);
        discoverBannerMainPresenter.fetchBanner();
    }


//    @Override
//    public void fetchBannerSuccess(ArrayList<SlideModel> discoverBannerMainList) {
//        imageSlider.setImageList(discoverBannerMainList, ScaleTypes.FIT);
//    }

    @Override
    public void fetchBannerSuccess(List<DiscoverBannerMain> discoverBannerMainList) {

    }

    @Override
    public void fetchBannerError(String error) {
        //Toast.makeText(getContext(), ""+error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String search = etSearch.getText().toString();
        if (search.isEmpty()) {
            frameUsers.setVisibility(View.GONE);
        } else {
            frameUsers.setVisibility(View.VISIBLE);
            doSearch(search);
        }
    }

    private void doSearch(String search) {
        SearchPresenter searchPresenter = new SearchPresenter(getContext(), DiscoverFragment.this);
        if (firebaseAuth.getCurrentUser() == null) {
            searchPresenter.fetchSearchQuery("", 0, search);
        } else {
            searchPresenter.fetchSearchQuery(firebaseAuth.getCurrentUser().getUid(), 0, search);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void searchSuccess(List<SearchResponse> searchResponseList) {
        SearchAdapter searchAdapter = new SearchAdapter(getContext(), searchResponseList);
        searchAdapter.notifyDataSetChanged();
        recyclerSearch.setAdapter(searchAdapter);
        recyclerSearch.setVisibility(View.VISIBLE);
        tvNoData.setVisibility(View.GONE);
    }

    @Override
    public void searchFailed(String error) {
        recyclerSearch.setVisibility(View.GONE);
        tvNoData.setVisibility(View.VISIBLE);
    }

}
