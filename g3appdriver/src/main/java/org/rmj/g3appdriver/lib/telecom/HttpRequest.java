package org.rmj.g3appdriver.lib.telecom;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;
import org.rmj.g3appdriver.utils.WebClient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.HashMap;

public class HttpRequest extends WebClient {
    private static final String TAG = HttpRequest.class.getSimpleName();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void sendRequest(String URL, HttpRequest.onServerResponseListener listener){
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
                    JSONObject errorResponse = new JSONObject(error);
                    String lsErrCde = errorResponse.getString("code");
                    listener.onErrorResponse(getErrorMessage(lsErrCde, error));
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
     * //@param jsonResponse error message from server must be parse to get the specific message and error code
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
     *//*
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
        } else if{
            return getErrMsg(lsErrCde);
        }
        return Message;
    }*/

    private String getErrorMessage(String err_code, String error){
        if(getErrMsg(err_code) != null){
            return getErrMsg(err_code);
        }
        return error;
    }

    private String getErrMsg(String err_code){

        switch(err_code){
            //HTYP Error Code
            case "400":
                return "The client has issued an invalid request. ";

            case "401":
                return "Authorization for the API is required, but the request has not been authenticated.";

            case "403":
                return "The request has been authenticated but does not have the appropriate permissions, or the requested resource cannot be found.";

            case "404":
                return "Requested path does not exist.";

            case "406":
                return "The client has requested a MIME type via the Accept header for a value not supported by the server.";

            case "422":
                return "The client has made a valid request, but the server cannot process it. This is often returned for APIs when certain limits have been exceeded.";

            case "429":
                return "The client has exceeded the number of requests allowed for a given time window.";

            case "500":
                return "An unexpected error on the SmartThings servers has occurred. ";

            case "501":
                return "The client request was valid and understood by the server, but the requested feature has yet to be implemented.";

            //Message and description
            case "4010000":
                return "The API key is not valid or the restriction data does not match.";

            case "4002102":
                return "IP address or http referer format are incorrect.";

            case "4042101":
                return "The IP address or http referer are empty.";

            case "4040100":
                return "There is no user linked to the API key.";

            case "4040900":
                return "There is no tenant linked to the API key.";

            case "4000000":
                return "Argument is invalid. Please enter valid Device IMEI.";

            case "4001809":
                return "There are no additional licenses that can be activated.";

            case "4040300":
                //message = "Error Code 4040300 (Device Not Found): A device was not found with the specified condition.";
                return "Specified Device IMEI was not found. Please contact MIS Support for uploading Device IMEI.";

            case "4000310":
                //message = "Error Code 4040300 (Device State Invalid): The operation is not permitted, in the current state.";
                return "Device IMEI was already activated. Please enroll your the device.";

            case "4000316":
                return "The bulk operation limit has been exceeded.";

            case "4040400":
                return "Internal profile not found.";

            case "4001805":
                return "License name already exists.";

            case "5001804":
                return "Error communication with the license server.";

            case "4001813":
                return "This license key is already registered to another region (EU/US).";

            case "4001802":
                return "Max trial license count reached.";

            case "4041800":
                return "License not found.";

            case "4001806":
                return "This device is already registered with a license.";

            case "4001812":
                return "License already deleted.";

            default:
                return null;
        }
    }
}
