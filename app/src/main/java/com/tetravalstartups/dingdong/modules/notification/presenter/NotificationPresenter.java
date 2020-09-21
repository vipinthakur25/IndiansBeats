package com.tetravalstartups.dingdong.modules.notification.presenter;

import android.content.Context;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.notification.model.Notification;

import java.util.ArrayList;

public class NotificationPresenter {
    private Context context;
    private INotify iNotify;

    public NotificationPresenter(Context context, INotify iNotify) {
        this.context = context;
        this.iNotify = iNotify;
    }

    public interface INotify {
        void notificationsNotFound(String message);

        void notificationsFound(ArrayList<Notification> notificationArrayList);
    }

    public void fetchNotification() {
        Master master = new Master(context);
        ArrayList<Notification> notificationArrayList = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Query query = db.collection("notification").whereEqualTo("receiver_user_id", master.getId());
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.getDocuments().isEmpty()) {
                    iNotify.notificationsNotFound("No Notifications");
                } else {
                    notificationArrayList.clear();
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Notification notification = snapshot.toObject(Notification.class);
                        notificationArrayList.add(notification);
                    }
                    iNotify.notificationsFound(notificationArrayList);
                }
            }
        });
    }

}
