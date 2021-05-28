package org.rmj.ggc.telecom.ServiceHandler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.rmj.ggc.telecom.Activity.Activity_Dashboard;
import org.rmj.ggc.telecom.Activity.Activity_Splash;

public class ActionBroadcast extends BroadcastReceiver {
    private static final String TAG = ActionBroadcast.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "Starting MainActivity");
        Intent i = new Intent(context, getActivity(context));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        new Activity_Splash().getInstance().finish();
    }

    private Class<? extends AppCompatActivity> getActivity(Context context){
        /*if(new SessionManager(context).isFirstLaunch()){
            return WelcomeLauncherActivity.class;
        } else {*/
            return Activity_Dashboard.class;
        //}
    }
}
