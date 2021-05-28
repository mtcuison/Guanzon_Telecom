package org.rmj.g3appdriver.dev;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;

import org.rmj.g3appdriver.etc.SharedPref;

public class Telephony {

    private Context mContext;

    private AppData appData;

    public Telephony(Context context){
        this.mContext = context;
        this.appData = AppData.getInstance(mContext);
    }


    @SuppressLint("HardwareIds")
    public String getDeviceID() {
        return Settings.Secure.getString(
                mContext.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public String getMobileNoDb(){
        Cursor cursor = appData.getReadableDb().rawQuery("SELECT * FROM "+ getTableName()+"",null);
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            String lnMobileNo = cursor.getString(cursor.getColumnIndex("sMobileNo"));
            cursor.close();
            return lnMobileNo;
        } else {
            cursor.close();
            return new SharedPref(mContext).getMobileNo();
        }
    }

    private String getTableName(){
        String ProductID = new SharedPref(mContext).ProducID();
        switch (ProductID){
            case "GuanzonApp":
                return "Client_Info_Master";
            case "Telecom":
            case "IntegSys":
                return "User_Info_Master";
        }
        return "";
    }
}
