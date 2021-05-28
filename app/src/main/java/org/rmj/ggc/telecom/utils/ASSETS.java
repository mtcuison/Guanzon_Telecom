package org.rmj.ggc.telecom.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.rmj.ggc.telecom.R;

public class ASSETS {
    public static Bitmap LargeNotificationIcon(Context context, String MessageType){
        switch (MessageType){
            case "00001":
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_security_warning);
            case "00002":
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_accoung_warning);
            case "00003":
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_accoung_warning);
            case "00004":
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_registry_status);
            case "00005":
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_preorder_ntc);
        }
        return null;
    }
}
