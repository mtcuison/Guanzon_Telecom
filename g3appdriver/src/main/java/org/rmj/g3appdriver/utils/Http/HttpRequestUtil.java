package org.rmj.g3appdriver.utils.Http;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.HashMap;

public class HttpRequestUtil extends WebClient {
    private static final String TAG = HttpRequestUtil.class.getSimpleName();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void sendRequest(String URL, onServerResponseListener listener){
        try{
            Log.e(TAG, "Http Request is created. Sending request to " + URL);
            /**
             * mListener.setData() is a JSONObject parameter which is return
             * by the class that implementing this class...*/

            String response = httpsPostJSon(URL, listener.setData().toString(), listener.setHeaders());
            Log.e(TAG, "Http Request has been sent successfully.");

            /**
             * if response == null
             * this indicates that the server is not responding
             * or no connection to remote server.*/
            if(response == null){
                /**
                 * Set the error response parameter
                 * inside the onErrorResponse...*/
                listener.onErrorResponse("Unable to connect to our remote server.");
                Log.e(TAG, "Server no response. Server connection is not establish/Server is not active.");
            } else {
                JSONObject jsonResponse = new JSONObject(response);
                Log.e(TAG, "Server response : " + jsonResponse.toString());
                String serverResult = jsonResponse.getString("result");
                if(serverResult.equalsIgnoreCase("success")){
                    /**
                     * Set the success server Response to the
                     * onResponse mListener parameter...
                     * this purpose is for the class implementor can easily
                     * manipulate the data coming frm server.*/
                    listener.onResponse(jsonResponse);
                } else {
                    String error = jsonResponse.getString("error");
                    listener.onErrorResponse(getErrorMessage(error));
                    Log.e(TAG, "Http Request has been sent with some error response.");
                }
            }
        } catch (MalformedURLException e) {
            listener.onErrorResponse("Something went wrong. Unknown network error has occurred." );
            e.printStackTrace();
        } catch (UnknownHostException e){
            listener.onErrorResponse("Something went wrong. Unable to reach our remote servers." );
            e.printStackTrace();
        } catch (IOException e) {
            listener.onErrorResponse("Something went wrong. Please check your connection" );
            e.printStackTrace();
        } catch (JSONException e) {
            listener.onErrorResponse("Something went wrong. Please use alphanumeric characters only." );
            e.printStackTrace();
        } catch (Exception e){
            listener.onErrorResponse("Something went wrong. Please try again later." );
            e.printStackTrace();
        }
    }

    public interface onServerResponseListener{
        HashMap setHeaders();
        JSONObject setData();
        void onResponse(JSONObject jsonResponse);
        void onErrorResponse(String message);
    }

    /**
     *
     * @param jsonResponse error message from server must be parse to get the specific message and error code
     * @return returns error message or json object...
     * @throws JSONException
     *
     * if Class that implements this method is Login of GuanzonApp
     * returns the error code if the account that login is not yet activated
     * else if the account has other error code return the error message...
     * if the error returns 40003 this indicates that the account was not yet activated.
     * return the otp to the main method...
     *
     * For Importing data
     * if server response returns error code 40026 this indicates that
     * no data was found on database.
     */
    private String getErrorMessage(String jsonResponse) throws JSONException{
        JSONObject errorResponse = new JSONObject(jsonResponse);
        String Message = errorResponse.getString("message");
        String lsErrCde = errorResponse.getString("code");
        if(lsErrCde.equalsIgnoreCase("40003")){
            return jsonResponse;
        } else if(lsErrCde.equalsIgnoreCase("40026")){
            return jsonResponse;
        } else if(lsErrCde.equalsIgnoreCase("2002")){
            return "Unable to connect to server. Please contact MIS Dept.";
        } else if(lsErrCde.equalsIgnoreCase("40004")){
            return jsonResponse;
        } else if(lsErrCde.equalsIgnoreCase("40012")){
            return "";
        } else if(lsErrCde.equalsIgnoreCase("40020")){
            return "Your login session is invalid.\nPlease re-login account to send your data to server.";
        }
        return Message;
    }
}
