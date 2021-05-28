package org.rmj.ggc.telecom.ServiceHandler;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.StrictMode;
import androidx.core.app.NotificationCompat;

import android.widget.Toast;

import com.rmj.ggc.telecom.R;

import org.rmj.g3appdriver.dev.AppData;
import org.rmj.g3appdriver.lib.integsys.CashCount;
import org.rmj.ggc.telecom.JavaExtras.SharedPref;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SyncService extends Service {

    /*String parameter
     * to be pass in cash count server*/
    private boolean Connected;
    private AppData sql_statements;
    private CashCount cashCount;
    private SharedPref sharedPref;

    /*Headers*/
    String ClientID;
    String UserID;
    String LogID;
    String firebase_token;

    private ArrayList<ArrayList<String>> empty_receieved = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> unsync_data = new ArrayList<ArrayList<String>>();

    @Override
    public void onCreate() {
        super.onCreate();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        sharedPref = new SharedPref(getApplicationContext());
        sql_statements = AppData.getInstance(getApplicationContext());
        ClientID = sql_statements.getClientID();
        UserID = sql_statements.getUserID();
        LogID = sql_statements.getLogNumber();
        firebase_token = sql_statements.getAppToken();

        cashCount = new CashCount(getApplicationContext(), sharedPref.DeviceID(), ClientID, sharedPref.ProducID(), UserID, LogID, firebase_token);
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }

    public SyncService() {
    }

    private class startSendingLocalData extends AsyncTask<String, Void, Integer>{
        @Override
        protected Integer doInBackground(String... strings) {
            Integer result = 0;
            while(result !=0) {
                try {
                    URL url = new URL("https://www.guanzongroup.com.ph/");

                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setRequestProperty("Connection", "close");
                    urlc.setConnectTimeout(3000); // mTimeout is in seconds
                    urlc.connect();

                    if (urlc.getResponseCode() == 200) {
                        result = 1;
                    } else {
                        result = 0;
                    }
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if(result == 1) {
                Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
                checkLocalData();
            }
            super.onPostExecute(result);
        }
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        IntentFilter intentFilter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(networkConnectionChecker, intentFilter);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private BroadcastReceiver networkConnectionChecker = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int connectionState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
                    WifiManager.WIFI_STATE_UNKNOWN);
            switch (connectionState) {
                case WifiManager.WIFI_STATE_ENABLED:
                    Connected = true;
                    checkLocalData();
                    break;
                case WifiManager.WIFI_STATE_DISABLED:
                    Connected = false;
                    break;
            }
        }
    };

    private void checkLocalData(){
        /*int indexes = sql_statements.getEmptyReceivedCount();
        if(indexes !=0) {
            cashCount.checkCashCountUpload();
            boolean checkcashresult = cashCount.checkCashCountUpload();
            Log.d("SyncService TAG", "Result: " + checkcashresult);
            notificationCall();
                if (cashCount.checkCashCountUpload()) {
                    cashCountResult();
                    //SyncServiceDialog(getApplicationContext()).show();
                }
        }*/
    }

    private void notificationCall(){

        NotificationCompat.Builder syncNotification = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.ic_guanzon_logo)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_guanzon_logo))
                .setContentTitle("Server Connection has been establish")
                .setContentText("Checking local data...");

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, syncNotification.build());
    }

    private void cashCountResult(){

        NotificationCompat.Builder syncNotification = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.ic_guanzon_logo)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_guanzon_logo))
                .setContentTitle("Server Connection has been establish")
                .setContentText("Cash count local data sent. Restart App to update List.");

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, syncNotification.build());
    }

    private AlertDialog SyncServiceDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Cash Count Local Data.")
                .setMessage("Cash Count Local Data has been sent to server.")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }
}
