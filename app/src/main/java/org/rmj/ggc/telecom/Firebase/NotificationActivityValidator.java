package org.rmj.ggc.telecom.Firebase;

import android.app.Activity;

public class NotificationActivityValidator {

    public boolean isActivityOpen(Activity activity){
        if(activity.getWindow().getDecorView().isShown()){
            return true;
        } else {
            return false;
        }
    }
}
