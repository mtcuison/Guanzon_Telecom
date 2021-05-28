package org.rmj.ggc.telecom.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rmj.ggc.telecom.R;

import org.rmj.androidprojects.guanzongroup.g3creditapp.Local.AssetImportUpdate.Import_Branches;
import org.rmj.androidprojects.guanzongroup.g3creditapp.Local.AssetImportUpdate.OnImportAssetListener;
import org.rmj.androidprojects.guanzongroup.g3creditapp.Local.LocalData.CreditAppLocalData;
import org.rmj.androidprojects.guanzongroup.g3logindriver.Activity_Login;
import org.rmj.g3appdriver.dev.AppData;
import org.rmj.g3appdriver.dev.Telephony;
import org.rmj.g3appdriver.etc.SessionManager;
import org.rmj.g3appdriver.utils.InAppUpdate;
import org.rmj.g3cm.android.g3cashmanager.LocalData.CashCountLocalData;
import org.rmj.ggc.g3msg_notifylib.DbHelper.NotificationData;
import org.rmj.ggc.telecom.Firebase.MyFirebaseMessagingService;
import org.rmj.ggc.telecom.JavaExtras.SharedPref;
import org.rmj.ggc.telecom.ServiceHandler.ActionBroadcast;
import org.rmj.ggc.telecom.ServiceHandler.Telecom_Service;
import org.rmj.ggc.telecom.utils.SessionExpiration;
import org.rmj.guanzongroup.promotions.Local.PromoLocalData;

public class Activity_Splash extends AppCompatActivity {
    private static final String TAG = Activity_Splash.class.getSimpleName();
    @SuppressLint("StaticFieldLeak")
    private static Activity_Splash instance;
    private ActionBroadcast loLoginRes;
    SharedPref sharedPref;
    SessionManager firstTimeLaunch;
    AppData sql_statements;
    Telephony telephony;

    private ProgressBar progressBar;
    private TextView lblStatusPrct;

    String[] PERMISSIONS = {
            Manifest.permission.INTERNET,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.VIBRATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new InAppUpdate(Activity_Splash.this, Activity_Splash.this).Check_Update();
        instance = this;
        loLoginRes = new ActionBroadcast();
        setupWidgets();
        setupJavaClassess();
        transparentToolbar();
        checkPermissions();
        new LoadScreenTask().execute(10);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onStart() {
        IntentFilter intentFilter = new IntentFilter("android.intent.action.TELECOM_SUCCESS_LOGIN");
        registerReceiver(loLoginRes, intentFilter);
        Log.e(TAG, "Action receiver has been registered.");
        scheduleJob();
        super.onStart();
    }

    private void setupWidgets(){
        progressBar = findViewById(R.id.pb_splashBar);
        TextView lblStatusView = findViewById(R.id.lbl_splText);
        lblStatusPrct = findViewById(R.id.lbl_splPercent);
    }

    private void setupJavaClassess(){
        telephony = new Telephony(Activity_Splash.this);
        sharedPref = new SharedPref(getApplicationContext());
        firstTimeLaunch = new SessionManager(getApplicationContext());
        sql_statements = AppData.getInstance(Activity_Splash.this);
    }

    private void getAppAssets(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String productID = "Telecom";
        sharedPref.setTemp_ProductID(productID);
        sharedPref.setTemp_DeviceID(telephony.getDeviceID());
        sharedPref.setDashBoadActivity("org.rmj.guanzongroup.telecom.Activity.Activity_Dashboard");
        new CreditAppLocalData(Activity_Splash.this).setupCreditAppData();
        new CashCountLocalData(Activity_Splash.this).setupCashCountData();
        new NotificationData(Activity_Splash.this).setupNotificationData();
        new PromoLocalData(Activity_Splash.this).createPromoTable();
    }

    private void checkPermissions() {
        while (!hasPermissions(Activity_Splash.this, PERMISSIONS)) {
            int PERMISSION_ALL = 10;
            ActivityCompat.requestPermissions(Activity_Splash.this, PERMISSIONS, PERMISSION_ALL);
        }
        getAppAssets();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(loLoginRes);
        Log.e(TAG, "Action receiver has been unregistered.");
        super.onDestroy();
    }

    public static Activity_Splash getInstance(){
        return instance;
    }

    public static boolean hasPermissions(Context context, String...permissions){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && context !=null && permissions != null) {
            for(String permission: permissions){
                if(ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
        }
        return true;
    }

    private void transparentToolbar() {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @SuppressLint("StaticFieldLeak")
    class LoadScreenTask extends AsyncTask<Integer, Integer, String>{

        @Override
        protected void onPreExecute() {
            startService(new Intent(Activity_Splash.this, MyFirebaseMessagingService.class));
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... integer) {
            for(int x = 0; x < integer[0]; x++){
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                publishProgress((int) ((x / (float) integer[0]) * 100));
            }
            if(!firstTimeLaunch.isLoggedIn()) {
                Intent intent = new Intent(Activity_Splash.this, Activity_Login.class);
                startActivity(intent);
            } else {
                boolean isSessionValid = new SessionExpiration(Activity_Splash.this).isSessionExpired();
                if(!isSessionValid){
                    Intent intent = new Intent(Activity_Splash.this, Activity_Login.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Activity_Splash.this, Activity_Dashboard.class);
                    startActivity(intent);
                    finish();
                }
            }
            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
            lblStatusPrct.setText(values[0].toString());
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            this.cancel(true);
            super.onPostExecute(s);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == InAppUpdate.InAppUpdateResult.RESULT_CODE){
            if(resultCode != RESULT_OK){
                System.exit(1);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void scheduleJob(){
        ComponentName loComponent = new ComponentName(this, Telecom_Service.class);
        JobInfo loJob = new JobInfo.Builder(123, loComponent)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .setPeriodic(900000)
                .build();
        JobScheduler loScheduler = (JobScheduler)getSystemService(JOB_SCHEDULER_SERVICE);
        int liResult = loScheduler.schedule(loJob);
        if(liResult == JobScheduler.RESULT_SUCCESS){
            Log.e(TAG, "Job Scheduled");
        } else {
            Log.e(TAG, "Job Schedule Failed.");
        }
    }
}