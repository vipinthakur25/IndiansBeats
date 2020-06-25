package com.tetravalstartups.dingdong.modules.create.filters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tetravalstartups.dingdong.R;

import java.util.List;

public class CameraFilterAdapter extends RecyclerView.Adapter<CameraFilterAdapter.ViewHolder> {

    Context context;
    List<CameraFilter> cameraFilterList;
    ICameraFilterAdapterListener iCameraFilterAdapterListener;

    public CameraFilterAdapter(Context context, List<CameraFilter> cameraFilterList, ICameraFilterAdapterListener iCameraFilterAdapterListener) {
        this.context = context;
        this.cameraFilterList = cameraFilterList;
        this.iCameraFilterAdapterListener = iCameraFilterAdapterListener;
    }

    @NonNull
    @Override
    public CameraFilterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.camera_filter_list_item,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CameraFilterAdapter.ViewHolder holder, int position) {
        CameraFilter cameraFilter = cameraFilterList.get(position);
        holder.tvFilterName.setText(cameraFilter.getName());
        holder.ivFilterEffect.setImageDrawable(context.getResources().getDrawable(cameraFilter.getSample()));
        holder.lvCameraFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iCameraFilterAdapterListener.onClick(cameraFilter.getId());
            }
        });
    }

    public interface ICameraFilterAdapterListener{
        void onClick(int id);
    }

    @Override
    public int getItemCount() {
        return cameraFilterList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout lvCameraFilters;
        private ImageView ivFilterEffect;
        private TextView tvFilterName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lvCameraFilters = itemView.findViewById(R.id.lvCameraFilters);
            ivFilterEffect = itemView.findViewById(R.id.ivFilterEffect);
            tvFilterName = itemView.findViewById(R.id.tvFilterName);
        }
    }
}
