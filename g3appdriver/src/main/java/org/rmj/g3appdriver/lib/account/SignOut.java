package org.rmj.g3appdriver.lib.account;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.rmj.g3appdriver.utils.SQLUtil;
import org.rmj.g3appdriver.utils.SecUtil;
import org.rmj.g3appdriver.utils.Http.WebClient;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static org.rmj.g3appdriver.etc.SQLiteHandler.TAG;

public class SignOut {
    private String dialog_result = "";

    private String message = "";
    private String imei;
    private String productid;
    private String userid;
    private String token;
    Context ctx;

    public SignOut(Context sctx, String simei, String sproductid, String suserid, String stoken){
        this.ctx = sctx;
        this.imei = simei;
        this.productid = sproductid;
        this.userid = suserid;
        this.token = stoken;

    }

    //result for dialog
    public String getResult(){
        return dialog_result;
    }

    //getting message
    public String getMessage(){
        return message;
    }

    public void signout(){

        String sURL = "https://restgk.guanzongroup.com.ph/security/signout.php";
        Calendar calendar = Calendar.getInstance();

        Map<String, String> headers =new HashMap<String, String>();

        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("g-api-id", productid);
        headers.put("g-api-imei", imei);
        headers.put("g-api-key", SQLUtil.dateFormat(calendar.getTime(), "yyyyMMddHHmmss"));
        String hash_toLower = SecUtil.md5Hex(headers.get("g-api-imei") + headers.get("g-api-key"));
        hash_toLower = hash_toLower.toLowerCase();
        headers.put("g-api-hash", hash_toLower);
        headers.put("g-api-user", userid);
        headers.put("g-api-token", token);

        //Create the parameters needed by the API
        JSONObject param = new JSONObject();
        try {
            param.put("", "");
        }catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject json_obj = null;

        String response = null;
        try {
            Log.d(TAG, " URL ACTIVATION: " + sURL);
            response = WebClient.httpsPostJSon(sURL, param.toString(), (HashMap<String, String>) headers);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(response == null){
            System.out.println("HTTP Error detected: " + System.getProperty("store.error.info"));
            if(System.getProperty("store.error.info").equals("404")){
                message = "Authorization Error " + System.getProperty("store.error.info") + ": Please make sure internet is stable. ";
                dialog_result = "error";
            }else{
                message = "Authorization Error " +  System.getProperty("store.error.info") + ": Please make sure the internet connection is stable.";
                dialog_result = "error";
            }
        }else{
            try {
                JSONObject jObj = new JSONObject(response);
                String result = jObj.getString("result");

                if(result.equals("success")){
                    message = "Success";
                    //DO SOMETHING

                }else{
                    Log.d(TAG, "Else: Json Result : " + response);
                    message = "Else Error";
                }
            } catch (JSONException e) {
                Log.d(TAG, "Catch: Json Result : " + response);
                message = "Catch Error";

            }
        }
    }
}
