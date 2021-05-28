package org.rmj.ggc.telecom.JavaExtras;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;

import com.rmj.ggc.telecom.R;

import org.rmj.ggc.telecom.Activity.Activity_Dashboard;

public class NotificationActivity {

    private static final int NOTIFICATION_ID_OPEN_ACTIVITY = 9;

    public static void openActivityNotification(Context context){
        NotificationCompat.Builder nc = new NotificationCompat.Builder(context);
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notifyIntent = new Intent(context, Activity_Dashboard.class);

        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        nc.setContentIntent(pendingIntent);

        nc.setSmallIcon(R.mipmap.ic_guanzon_logo);
        nc.setAutoCancel(true);
        nc.setContentTitle("Server Connection has established");
        nc.setContentText("Syncing Local Data to Server. Tap here to see status");

        nm.notify(NOTIFICATION_ID_OPEN_ACTIVITY, nc.build());
    }
}
