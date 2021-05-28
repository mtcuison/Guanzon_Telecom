package org.rmj.g3appdriver.lib.telecom;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;
import org.rmj.g3appdriver.utils.ConnectionUtil;
import org.rmj.g3appdriver.utils.Http.RequestHeaders;

import java.util.HashMap;

public class Knox {
    private static final String TAG = Knox.class.getSimpleName();
    private static final String KNOX_URL = "https://restgk.guanzongroup.com.ph/samsung/knox.php";
    private RequestHeaders headers;
    private onKnoxRequestListener onKnoxRequestListener;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void sendKnoxRequest(Context context, final String parameters, final String RequestType, onKnoxRequestListener listener){
        this.onKnoxRequestListener = listener;
        ConnectionUtil connectionUtil = new ConnectionUtil(context);
        headers = new RequestHeaders(context);
        if(connectionUtil.isDeviceConnected()) {
            new HttpRequest().sendRequest(KNOX_URL, new HttpRequest.onServerResponseListener() {
                @Override
                public HashMap setHeaders() {
                    return (HashMap) headers.getHeaders();
                }

                @Override
                public JSONObject setData() {
                    JSONObject request = new JSONObject();
                    try {
                        request.put("request", RequestType);
                        request.put("param", parameters);
                        Log.e(TAG, "Knox parameters has been created." + request.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return request;
                }

                @Override
                public void onResponse(JSONObject jsonResponse) {
                    Log.e(TAG, "Server response has been receive. Result : SUCCESS");
                    if(RequestType.equalsIgnoreCase(KnoxRequest.UNLOCK_REQUEST) ||
                    RequestType.equalsIgnoreCase(KnoxRequest.OFFLINE_PIN_REQUEST) ||
                    RequestType.equalsIgnoreCase(KnoxRequest.GET_DEVICE_LOG_REQUEST)){
                        onKnoxRequestListener.onSuccessResult(jsonResponse.toString());
                    } else {
                        onKnoxRequestListener.onSuccessResult("Knox registration has finish successfully.");
                    }
                }

                @Override
                public void onErrorResponse(String message) {
                    Log.e(TAG, "Server response has been receive with errors. Result : ERROR ");
                    Log.e(TAG, "Knox response error message : " + message);
                    onKnoxRequestListener.onErrorResult(message);
                }
            });
        } else {
            onKnoxRequestListener.onErrorResult("Unable to reach our server. Please make sure you are connected to the internet.");
        }
    }

    public interface onKnoxRequestListener{
        void onSuccessResult(String Result);
        void onErrorResult(String errorMessage);
    }

    public interface KnoxRequest{
        String ACTIVATE_REQUEST = "DEVICES_APPROVE";
        String GET_PIN_REQUEST = "DEVICES_GETPIN";
        String UNLOCK_REQUEST = "DEVICES_UNLOCK";
        String OFFLINE_PIN_REQUEST = "DEVICES_OFFLINE_PIN";
        String GET_DEVICE_LOG_REQUEST = "DEVICES_GETDEVICELOG";
    }
}
