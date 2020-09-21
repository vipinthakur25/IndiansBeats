package com.tetravalstartups.dingdong.modules.comment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.notification.model.Notification;
import com.tetravalstartups.dingdong.modules.passbook.BuyCoinBottomSheetFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InVideoCommentBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {

    private View view;
    private RecyclerView recyclerComments;
    private SharedPreferences commentsPref;
    private FirebaseFirestore db;
    private TextView tvCommentsCount;
    private TextView tvNoComments;
    private EditText etComments;
    private ImageView ivPostComment;
    private Master master;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.in_video_comment_bottom_sheet, container, false);
        initView();
        return view;
    }

    private void initView() {
        commentsPref = getContext().getSharedPreferences("comments", 0);
        master = new Master(getContext());
        db = FirebaseFirestore.getInstance();
        recyclerComments = view.findViewById(R.id.recyclerComments);
        tvCommentsCount = view.findViewById(R.id.tvCommentsCount);
        etComments = view.findViewById(R.id.etComments);
        tvNoComments = view.findViewById(R.id.tvNoComments);
        ivPostComment = view.findViewById(R.id.ivPostComment);
        ivPostComment.setOnClickListener(this);
        fetchComments(commentsPref.getString("video_id", "none"));
    }

    private void fetchComments(String video_id) {
        recyclerComments.setLayoutManager(new LinearLayoutManager(getContext()));
        List<InVideoComment> inVideoCommentList = new ArrayList<>();
        Query query = db.collection("videos")
                .document(video_id)
                .collection("comments");

        query.orderBy("timestamp", Query.Direction.DESCENDING)
               .addSnapshotListener(new EventListener<QuerySnapshot>() {
                   @Override
                   public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                       int comments_count = queryDocumentSnapshots.getDocuments().size();
                       tvCommentsCount.setText("Comments ("+comments_count+")");

                       HashMap hashMap = new HashMap();
                       hashMap.put("comment_count", comments_count+"");
                       db.collection("videos")
                               .document(video_id)
                               .update(hashMap);

                       if (queryDocumentSnapshots.getDocuments().isEmpty()){
                           tvNoComments.setVisibility(View.VISIBLE);
                           recyclerComments.setVisibility(View.GONE);
                       } else {
                           inVideoCommentList.clear();
                           for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                               InVideoComment inVideoComment = snapshot.toObject(InVideoComment.class);
                               inVideoCommentList.add(inVideoComment);
                           }

                           InVideoCommentAdapter inVideoCommentAdapter =
                                   new InVideoCommentAdapter(getContext(), inVideoCommentList);
                           inVideoCommentAdapter.notifyDataSetChanged();
                           recyclerComments.setAdapter(inVideoCommentAdapter);
                           recyclerComments.setVisibility(View.VISIBLE);
                           tvNoComments.setVisibility(View.GONE);
                       }
                   }
               });

    }

    private void postComment(String video_id, String message) {

        DocumentReference documentReference = db.collection("videos").document();
        String id = documentReference.getId();

        InVideoComment inVideoComment = new InVideoComment();
        inVideoComment.setId(id);
        inVideoComment.setMessage(message);
        inVideoComment.setUser_handle(master.getHandle());
        inVideoComment.setUser_photo(master.getPhoto());
        inVideoComment.setUser_id(master.getId());
        inVideoComment.setVideo_id(video_id);

        db.collection("videos")
                .document(video_id)
                .collection("comments")
                .document(id)
                .set(inVideoComment);

        db.collection("notification").document();
        String notifyID = documentReference.getId();

        Notification notification = new Notification();
        notification.setId(notifyID);
        notification.setType(Notification.NOTIFICATION_TYPE_LIKE);
        notification.setSender_user_id(master.getId());
        notification.setSender_user_photo(master.getPhoto());
        notification.setSender_user_handle(master.getHandle());
        notification.setReceiver_user_id(commentsPref.getString("user_id", ""));
        notification.setReceiver_user_photo(commentsPref.getString("user_photo", ""));
        notification.setVideo_id(video_id);
        notification.setVideo_thumbnail(commentsPref.getString("video_thumbnail", ""));
        notification.setAmount("");

        db.collection("notification")
                .document(notifyID)
                .set(notification);

    }

    @Override
    public void onClick(View v) {
        if (v == ivPostComment) {
            String message = etComments.getText().toString();
            if (message.isEmpty()) {
                Toast.makeText(getContext(), "Comments must not be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            postComment(commentsPref.getString("video_id", "none"), message);
            etComments.setText("");
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

}
