package org.rmj.ggc.telecom.ServiceHandler;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;
import org.rmj.androidprojects.guanzongroup.g3creditapp.Local.AssetImportUpdate.ImportInstance;
import org.rmj.androidprojects.guanzongroup.g3creditapp.Local.AssetImportUpdate.Import_ProvinceAsset;
import org.rmj.androidprojects.guanzongroup.g3creditapp.Local.AssetImportUpdate.Import_TownAsset;
import org.rmj.androidprojects.guanzongroup.g3creditapp.Local.AssetImportUpdate.OnImportAssetListener;
import org.rmj.g3appdriver.dev.AppData;
import org.rmj.g3appdriver.lib.account.AppLogin.Telecom;
import org.rmj.g3appdriver.utils.ConnectionUtil;
import org.rmj.g3appdriver.utils.Http.RequestHeaders;
import org.rmj.g3appdriver.utils.WebClient;
import org.rmj.guanzongroup.promotions.Local.Import_RaffleBasis;
import org.rmj.guanzongroup.promotions.Local.PromoLocalData;
import org.rmj.guanzongroup.promotions.Model.PromoDetail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@SuppressLint("NewApi")
public class Telecom_Service extends JobService {
    public static final String TAG = Telecom_Service.class.getSimpleName();

    private AppData db;
    private ConnectionUtil conn;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.e(TAG, "Job Started");
        db = AppData.getInstance(Telecom_Service.this);
        conn = new ConnectionUtil(Telecom_Service.this);
        try {
            updatePromoEntries(jobParameters);
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.e(TAG, "Job Finished");
        return true;
    }


    private void updatePromoEntries(JobParameters params) throws Exception {
        ImportInstance[] imports = {new Import_ProvinceAsset(), new Import_TownAsset(), new Import_RaffleBasis()};
        for(int x = 0; x < imports.length; x++){
            imports[x].SendImportRequest(Telecom_Service.this, new OnImportAssetListener() {
                @Override
                public void onImportSuccess() {

                }

                @Override
                public void onImportFailed(String Message) {

                }
            });
        }
        try {
            List<JSONObject> entries = getList_PromoUrls();
            if (entries.size() > 0) {
                new sendRaffleEntryTask().execute(entries);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        jobFinished(params, false);
    }

    private List<JSONObject> getList_PromoUrls(){
        List<JSONObject> PromoUrl = new ArrayList<>();
        try {
            String[] args = {"0"};
            db.getReadableDb().beginTransaction();
            Cursor cursor = db.getReadableDb().rawQuery("SELECT * FROM PromoLocal_Detail WHERE cSendStat = ?", args);
            if (cursor.getCount() > 0) {
                Log.e(TAG, "Updating promo entries...");
                cursor.moveToFirst();
                for (int x = 0; x < cursor.getCount(); x++) {
                    JSONObject loJson = new JSONObject();
                    loJson.put("brc", cursor.getString(cursor.getColumnIndex("sBranchCd")));
                    loJson.put("typ", cursor.getString(cursor.getColumnIndex("sDocTypex")));
                    loJson.put("dte", cursor.getString(cursor.getColumnIndex("dTransact")));
                    loJson.put("nox", cursor.getString(cursor.getColumnIndex("sDocNoxxx")));
                    loJson.put("mob", cursor.getString(cursor.getColumnIndex("sMobileNo")));
                    loJson.put("nme", cursor.getString(cursor.getColumnIndex("sClientNm")));
                    loJson.put("add", cursor.getString(cursor.getColumnIndex("sAddressx")));
                    loJson.put("twn", cursor.getString(cursor.getColumnIndex("sTownIDxx")));
                    loJson.put("prv", cursor.getString(cursor.getColumnIndex("sProvIDxx")));
                    loJson.put("cid", "");
                    loJson.put("div", "");
                    loJson.put("ent", db.getUserID());
                    PromoUrl.add(loJson);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            db.getReadableDb().setTransactionSuccessful();
            db.getReadableDb().endTransaction();
        } catch (Exception e){
            e.printStackTrace();
        }
        return PromoUrl;
    }

    private String getDateTransact(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    private class sendRaffleEntryTask extends AsyncTask<List<JSONObject>, Void, String> {
        private PromoLocalData promoLocalData;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            promoLocalData = new PromoLocalData(Telecom_Service.this);
        }

        @Override
        protected String doInBackground(List<JSONObject>... entries) {
            try {
                if (entries[0].size() > 0) {
                    for (int x = 0; x < entries[0].size(); x++) {
                        String lsUrl = "https://restgk.guanzongroup.com.ph/promo/fblike/encodex.php";
                        String response = WebClient.httpsPostJSon(lsUrl, entries[0].get(x).toString(), (HashMap<String, String>) new RequestHeaders(Telecom_Service.this).getHeaders());
                        JSONObject loResponse = new JSONObject(response);
                        PromoDetail detail = new PromoDetail();
                        detail.setsClientNm(entries[0].get(x).getString("nme"));
                        detail.setsDocNoxxx(entries[0].get(x).getString("nox"));
                        detail.setsDocTypex(entries[0].get(x).getString("typ"));
                        detail.setsMobileNo(entries[0].get(x).getString("mov"));
                        detail.setcSendStat('1');
                        detail.setsBranchCd(entries[0].get(x).getString("brc"));
                        detail.setdTransact(getDateTransact());
                        String result = loResponse.getString("result");
                        if (result.equalsIgnoreCase("success")) {
                            Log.e(TAG, "Promo entry of : " + entries[0].get(x).getString("nme") + "has been sent to server.");
                            //new CreditAppNotice(IntegSys_ServiceProvider.this, "Promo entry of : " + entries[0].get(x).getString("nme") + "has been sent to server.", CreditAppNotice.CHANNEL_ID.IndividualMessage).showNotification();
                            detail.setcSendStat('1');
                            detail.setdTransact(getDateTransact());
                            promoLocalData.updatePromoDetail(detail);
                        } else {
                            JSONObject loJson = loResponse.getJSONObject("error");
                            String lsCode = loJson.getString("code");
                            int nCase = Integer.parseInt(lsCode);
                            for (int i = 99; i < 107; i++) {
                                if (nCase == i) {
                                    detail.setcSendStat('3');
                                    detail.setdTransact(getDateTransact());
                                    promoLocalData.updatePromoDetail(detail);
                                    break;
                                }
                            }
                        }
                        Thread.sleep(1000);
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            return "null";
        }
    }
}
