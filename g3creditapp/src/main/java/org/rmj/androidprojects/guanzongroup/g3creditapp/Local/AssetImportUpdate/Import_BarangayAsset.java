package org.rmj.androidprojects.guanzongroup.g3creditapp.Local.AssetImportUpdate;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.rmj.androidprojects.guanzongroup.g3creditapp.Local.LocalData.CreditAppLocalData;
import org.rmj.g3appdriver.utils.ConnectionUtil;
import org.rmj.g3appdriver.utils.CreditAppAPI;
import org.rmj.g3appdriver.utils.Http.HttpRequestUtil;
import org.rmj.g3appdriver.utils.Http.RequestHeaders;

import java.util.HashMap;

public class Import_BarangayAsset implements ImportInstance {
    private static final String TAG = Import_BarangayAsset.class.getSimpleName();

    private Context mContext;
    private RequestHeaders headers;

    @Override
    public void SendImportRequest(Context context, final OnImportAssetListener listener) {
        this.mContext = context;
        ConnectionUtil connectionUtil = new ConnectionUtil(mContext);
        this.headers = new RequestHeaders(mContext);

        if(connectionUtil.isDeviceConnected()){
            new HttpRequestUtil().sendRequest(CreditAppAPI.URL_IMPORT_BARANGAY, new HttpRequestUtil.onServerResponseListener() {
                @Override
                public HashMap setHeaders() {
                    Log.e(TAG, "Headers has been initialized.");
                    return (HashMap) headers.getHeaders();
                }

                @Override
                public JSONObject setData() {
                    try{
                        return new UpdateSet(mContext).getDataParameter(UpdateSet.LocalTables.Barangay);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return new JSONObject();
                }

                @Override
                public void onResponse(JSONObject jsonResponse) {
                    Log.e(TAG, "Assets receive successfully.");
                    CreditAppLocalData localData = new CreditAppLocalData(mContext);
                    if(localData.InsertBarangay(jsonResponse)){
                        listener.onImportSuccess();
                    } else {
                        listener.onImportFailed(localData.getMessage());
                    }
                }

                @Override
                public void onErrorResponse(String message) {
                    Log.e(TAG, "unable to import Assets. Message : " + message);
                    listener.onImportFailed(message);
                }
            });
        } else {
            listener.onImportFailed("Unable to import asset. No internet connection.");
        }
    }
}
