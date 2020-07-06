package com.tetravalstartups.dingdong.modules.create.filters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.utils.EqualSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class CameraFilterBottomSheet extends BottomSheetDialogFragment implements CameraFilterAdapter.ICameraFilterAdapterListener {

    private View view;
    private FilterSelectedListener filterSelectedListener;
    private RecyclerView recyclerFilters;
    private List<CameraFilter> cameraFilterList;
    private CameraFilterAdapter cameraFilterAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.camera_filter_bottom_sheet, container, false);
        initView();
        return view;
    }

    private void initView() {
        recyclerFilters = view.findViewById(R.id.recyclerFilters);
        setFilterData();
    }

    private void setFilterData(){
        cameraFilterList = new ArrayList<>();
        recyclerFilters.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL,
                false));
        recyclerFilters.addItemDecoration(new EqualSpacingItemDecoration(16,
                EqualSpacingItemDecoration.HORIZONTAL));

        // add filter data
        cameraFilterList.add(new CameraFilter(0, "None", R.drawable.emma));
        cameraFilterList.add(new CameraFilter(1, "Auto Fix", R.drawable.emma));
        cameraFilterList.add(new CameraFilter(2, "B&W", R.drawable.emma));
        cameraFilterList.add(new CameraFilter(3, "Bright", R.drawable.emma));
        cameraFilterList.add(new CameraFilter(4, "Contrast", R.drawable.emma));
        cameraFilterList.add(new CameraFilter(5, "Cross", R.drawable.emma));
        cameraFilterList.add(new CameraFilter(6, "Document", R.drawable.emma));
        cameraFilterList.add(new CameraFilter(7, "Duotone", R.drawable.emma));
        cameraFilterList.add(new CameraFilter(8, "Fill", R.drawable.emma));
        cameraFilterList.add(new CameraFilter(9, "Gamma", R.drawable.emma));
        cameraFilterList.add(new CameraFilter(10, "Grain", R.drawable.emma));
        cameraFilterList.add(new CameraFilter(11, "Greyscale", R.drawable.emma));
        cameraFilterList.add(new CameraFilter(12, "Hue", R.drawable.emma));
        cameraFilterList.add(new CameraFilter(13, "Invert", R.drawable.emma));
        cameraFilterList.add(new CameraFilter(14, "Lomoish", R.drawable.emma));
        cameraFilterList.add(new CameraFilter(15, "Posterize", R.drawable.emma));
        cameraFilterList.add(new CameraFilter(16, "Saturation", R.drawable.emma));
        cameraFilterList.add(new CameraFilter(17, "Sepia", R.drawable.emma));
        cameraFilterList.add(new CameraFilter(18, "Sharp", R.drawable.emma));
        cameraFilterList.add(new CameraFilter(19, "Temp", R.drawable.emma));
        cameraFilterList.add(new CameraFilter(20, "Vintage", R.drawable.emma));

        cameraFilterAdapter =
                new CameraFilterAdapter(getContext(), cameraFilterList);

        cameraFilterAdapter.notifyDataSetChanged();

        recyclerFilters.setAdapter(cameraFilterAdapter);
    }

    @Override
    public void onClick(int id) {
        filterSelectedListener.onClicked(id);
    }

    public interface FilterSelectedListener {
        void onClicked(int id);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            filterSelectedListener = (CameraFilterBottomSheet.FilterSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }

    }


}
