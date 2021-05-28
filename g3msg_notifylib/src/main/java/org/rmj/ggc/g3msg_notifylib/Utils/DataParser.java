package org.rmj.ggc.g3msg_notifylib.Utils;

import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class DataParser {

    private RemoteMessage dataMessage;

    public DataParser(RemoteMessage Data){
        this.dataMessage = Data;
    }

    /**Parse the RemoteMessage receive from Firebase.
     * getValueof = this parses the remote message getting the JSONObject inside the remote message.
     * getDataValueOf = this parses the remote message getting the data inside the remote message itself.*/
    public String getValueOf(String JSONDataKeyValue){
        String msg_data = dataMessage.getData().get("msg_data");
        String value = "";
        try{
            JSONObject jsonData = new JSONObject(msg_data);
            value = jsonData.getString(JSONDataKeyValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }

    public String getDataValueOf(String RemoteMessageKeyValue){
        return dataMessage.getData().get(RemoteMessageKeyValue);
    }
}
