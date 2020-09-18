package com.tetravalstartups.dingdong.modules.discover;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tetravalstartups.dingdong.R;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    List<ContactModel> contactModelList;

    public ContactAdapter(List<ContactModel> contactModelList) {
        this.contactModelList = contactModelList;
    }

    @NonNull
    @Override
    public ContactAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ContactViewHolder holder, int position) {
        ContactModel contactModel = contactModelList.get(position);
        if (contactModel != null) {
            if (contactModel.getName() != null) {
                holder.tvName.setText(contactModel.getName());
            }
            if (contactModel.getNumber() != null) {
                StringBuffer buffer = new StringBuffer();
                for (String number : contactModel.getNumber()) {
                    buffer.append(number).append("\n");
                }
                holder.tvNumber.setText(buffer);
            }
        }
    }

    @Override
    public int getItemCount() {
        return contactModelList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNumber;
        private TextView tvName;
        private Button btnInvite;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNumber = itemView.findViewById(R.id.tvNumber);
            tvName = itemView.findViewById(R.id.tvName);
            btnInvite = itemView.findViewById(R.id.btnInvite);
        }
    }
}
