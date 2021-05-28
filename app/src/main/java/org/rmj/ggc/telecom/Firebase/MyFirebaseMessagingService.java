package org.rmj.ggc.telecom.Firebase;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.rmj.g3appdriver.dev.AppData;
import org.rmj.ggc.g3msg_notifylib.Builder.NotificationBuilder;
import org.rmj.ggc.g3msg_notifylib.Fragments.Fragment_NotificationList;
import org.rmj.ggc.g3msg_notifylib.Utils.Validator;
import org.rmj.ggc.telecom.Activity.Activity_Dashboard;

public class MyFirebaseMessagingService extends FirebaseMessagingService{
    private static String TAG = "MyFirebaseMessageService";
    private Validator validator;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        AppData appData = AppData.getInstance(MyFirebaseMessagingService.this);
        Log.e(TAG, "New Token RECIEVED"+ s);
        appData.setAppToken(s);
    }

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        NotificationBuilder builder = new NotificationBuilder(MyFirebaseMessagingService.this, remoteMessage);
        validator = new Validator(MyFirebaseMessagingService.this, remoteMessage);
        Log.d(TAG, "From SENDER: " + remoteMessage.getFrom());

        if(remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message Data: " + remoteMessage.getData());
            if(validator.VALID_NOTIFICATION()) {
                try {
                    /**
                     * NotificationBuilder sets the activity on which
                     * the user must be redirected...*/
                    builder.setIntent(new Intent(MyFirebaseMessagingService.this, Activity_Dashboard.class));
                    /**
                     * NotificationBuilder handles the notification being
                     * receive by the user...*/
                    builder.CreateNotification();
                    new Fragment_NotificationList().getInstance().refreshView();
                    new Activity_Dashboard().getInstance().setNotificationBadge();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
