package org.rmj.ggc.telecom.JavaExtras;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionHandler {

    private static String TAG = SessionHandler.class.getSimpleName();

    SharedPreferences pref;

    Editor editor;
    Context _context;

    int PRIVADE_MODE = 0;

    private static final String PREF_NAME = "GuanzonApps";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public SessionHandler(Context context){
        this._context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVADE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn){

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        editor.commit();

        Log.d(TAG, "User login session modified");
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public void setIsFirstTimeLaunch(boolean isFirstTimeLaunch){
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTimeLaunch);
        editor.commit();
    }

    public boolean isFirstTimeLaunch(){
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }
}
