package org.rmj.guanzongroup.promotions.Local;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.rmj.g3appdriver.dev.AppData;
import org.rmj.guanzongroup.promotions.Model.DocumentDetail;
import org.rmj.guanzongroup.promotions.Model.PromoDetail;
import org.rmj.guanzongroup.promotions.Model.TownInfo;

import java.util.ArrayList;
import java.util.List;

public class PromoLocalData {
    public static final String TAG = PromoLocalData.class.getSimpleName();

    private final AppData db;

    public PromoLocalData(Context context){
        db = AppData.getInstance(context);
    }

    public void createPromoTable(){
        db.getWritableDb().execSQL("CREATE TABLE IF NOT EXISTS PromoLocal_Detail(" +
                "sTransNox varchar," +
                "sBranchCd varchar," +
                "dTransact varchar," +
                "sClientNm varchar," +
                "sAddressx varchar," +
                "sTownIDxx varchar," +
                "sProvIDxx varchar," +
                "sDocTypex varchar," +
                "sDocNoxxx varchar," +
                "sMobileNo varchar," +
                "cSendStat char," +
                "dTimeStmp varchar," +
                "PRIMARY KEY(sBranchCd, dTransact))");

        db.getWritableDb().execSQL("CREATE TABLE IF NOT EXISTS FB_Raffle_Transaction_Basis(" +
                "sDivision varchar," +
                "sTableNme varchar," +
                "sReferCde varchar," +
                "sReferNme varchar," +
                "cCltInfox char," +
                "cRecdStat char," +
                "dModified varchar," +
                "PRIMARY KEY (sDivision, sTableNme, sReferCde))");
    }

    public boolean insertDocumentTypeDetail(List<DocumentDetail> documentDetail){
        try{
            String lsSql = "INSERT OR REPLACE INTO FB_Raffle_Transaction_Basis(sDivision," +
                    "sTableNme, " +
                    "sReferCde, " +
                    "sReferNme ," +
                    "cRecdStat, " +
                    "dModified) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            db.getWritableDb().beginTransaction();
            SQLiteStatement loSql = db.getWritableDb().compileStatement(lsSql);
            for(int x = 0; x < documentDetail.size(); x++){
                loSql.clearBindings();
                loSql.bindString(1, documentDetail.get(x).getsDivision());
                loSql.bindString(2, documentDetail.get(x).getsTableNme());
                loSql.bindString(3, documentDetail.get(x).getsReferCde());
                loSql.bindString(4, documentDetail.get(x).getsReferNme());
                loSql.bindString(5, documentDetail.get(x).getcRecdStat());
                loSql.bindString(6, documentDetail.get(x).getdModified());
                loSql.execute();
            }
            loSql.close();
            db.getWritableDb().setTransactionSuccessful();
            db.getWritableDb().endTransaction();
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public LiveData<List<DocumentDetail>> getDocumentTypes(String division){
        MutableLiveData<List<DocumentDetail>> docuList = new MutableLiveData<>();
        try{
            String[] args = {division};
            db.getReadableDb().beginTransaction();
            Cursor cursor = db.getReadableDb().rawQuery("SELECT * FROM FB_Raffle_Transaction_Basis WHERE cRecdStat = '1' AND sDivision = ? GROUP BY sReferNme ORDER BY sReferNme", args);
            if(cursor.getCount() > 0){
                cursor.moveToFirst();
                List<DocumentDetail> documentDetails = new ArrayList<>();
                for(int x =0; x < cursor.getCount(); x++){
                    DocumentDetail documentDetail = new DocumentDetail();
                    documentDetail.setsTableNme(cursor.getString(cursor.getColumnIndex("sTableNme")));
                    documentDetail.setsReferCde(cursor.getString(cursor.getColumnIndex("sReferCde")));
                    documentDetail.setsReferNme(cursor.getString(cursor.getColumnIndex("sReferNme")));
                    documentDetails.add(documentDetail);
                    cursor.moveToNext();
                }
                db.getReadableDb().setTransactionSuccessful();
                db.getReadableDb().endTransaction();
                docuList.setValue(documentDetails);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return docuList;
    }

    public LiveData<String[]> documentTypes(String division){
        MutableLiveData<String[]> docuList = new MutableLiveData<>();
        try{
            String[] args = {division};
            db.getReadableDb().beginTransaction();
            Cursor cursor = db.getReadableDb().rawQuery("SELECT sReferNme FROM FB_Raffle_Transaction_Basis WHERE cRecdStat = '1' AND sDivision = ? GROUP BY sReferNme ORDER BY sReferNme", args);
            if(cursor.getCount() > 0){
                cursor.moveToFirst();
                String[] documentDetails = new String[cursor.getCount()];
                for(int x = 0; x < cursor.getCount(); x++){
                    documentDetails[x] = cursor.getString(0);
                    cursor.moveToNext();
                }
                docuList.setValue(documentDetails);
            }
            db.getReadableDb().setTransactionSuccessful();
            db.getReadableDb().endTransaction();
        } catch (Exception e){
            e.printStackTrace();
        }
        return docuList;
    }

    public void insertPromoDetail(PromoDetail promoDetail){
        try {
            if(!isDuplicantEntry(promoDetail)) {
                db.getWritableDb().beginTransaction();
                String lsSql = "INSERT INTO PromoLocal_Detail(sBranchCd, " +
                        "dTransact, " +
                        "sClientNm, " +
                        "sAddressx, " +
                        "sTownIDxx, " +
                        "sProvIDxx, " +
                        "sDocTypex, " +
                        "sDocNoxxx, " +
                        "sMobileNo, " +
                        "cSendStat) " +
                        "VALUES (" +
                        "'" + promoDetail.getsBranchCd() + "', " +
                        "'" + promoDetail.getdTransact() + "', " +
                        "'" + promoDetail.getsClientNm() + "', " +
                        "'" + promoDetail.getsAddresxx() + "', " +
                        "'" + promoDetail.getsTownIDxx() + "', " +
                        "'" + promoDetail.getsProvIDxx() + "', " +
                        "'" + promoDetail.getsDocTypex() + "', " +
                        "'" + promoDetail.getsDocNoxxx() + "', " +
                        "'" + promoDetail.getsMobileNo() + "', " +
                        "'" + promoDetail.getcSendStat() + "' " +
                        ")";
                db.getWritableDb().execSQL(lsSql);
                db.getWritableDb().setTransactionSuccessful();
                db.getWritableDb().endTransaction();
                Log.e(TAG, "Promo entry has been save to local");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updatePromoDetail(PromoDetail promoDetail){
        try {
            db.getWritableDb().beginTransaction();
            String lsSql = "UPDATE PromoLocal_Detail SET sClientNm = '" + promoDetail.getsClientNm() + "', " +
                    "sDocTypex = '" + promoDetail.getsDocTypex() + "', " +
                    "sDocNoxxx = '" + promoDetail.getsDocNoxxx() + "', " +
                    "sMobileNo = '" + promoDetail.getsMobileNo() + "', " +
                    "cSendStat = '" + promoDetail.getcSendStat() + "', " +
                    "dTimeStmp = '" + promoDetail.getsTimeStmp() + "' " +
                    "WHERE sBranchCd = '" + promoDetail.getsBranchCd() + "' AND dTransact = '" + promoDetail.getdTransact() + "'";
            db.getWritableDb().execSQL(lsSql);
            db.getWritableDb().setTransactionSuccessful();
            db.getWritableDb().endTransaction();
            Log.e(TAG, "Promo entry has been updated");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean isDuplicantEntry(PromoDetail detail){
        boolean result = false;
        try{
            String[] args = {detail.getsClientNm(), detail.getsDocTypex(), detail.getsDocNoxxx(), detail.getsMobileNo()};
            db.getReadableDb().beginTransaction();
            Cursor cursor = db.getReadableDb().rawQuery("SELECT * FROM PromoLocal_Detail " +
                    "WHERE sClientNm = ? " +
                    "AND sDocTypex = ? " +
                    "AND sDocNoxxx = ? " +
                    "AND sMobileNo = ?", args);
            if(cursor.getCount()>0){
                result = true;
            }
            cursor.close();
            db.getReadableDb().setTransactionSuccessful();
            db.getReadableDb().endTransaction();
        } catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public LiveData<String[]> getTownInfoList(){
        MutableLiveData<String[]> infoList = new MutableLiveData<>();
        try{
            db.getReadableDb().beginTransaction();
            String lsSql = "SELECT" +
                    "  a.sTownIDxx" +
                    ", a.sTownName" +
                    ", b.sProvName" +
                    ", a.sZippCode" +
                    ", b.sProvIDxx" +
                    " FROM CreditApp_Town a" +
                    ", CreditApp_Province b" +
                    " WHERE a.sProvIDxx = b.sProvIDxx" +
                    " AND a.cRecdStat = '1'" +
                    " AND b.cRecdStat = '1'";
            Cursor cursor = db.getReadableDb().rawQuery(lsSql, null);
            if(cursor.getCount() > 0){
                cursor.moveToFirst();
                String[] townInfos = new String[cursor.getCount()];
                for(int x = 0; x < cursor.getCount(); x++){
                    String lsTownInfo = cursor.getString(1) + ", " + cursor.getString(2) + ", " + cursor.getString(3);
                    townInfos[x] = lsTownInfo;
                    cursor.moveToNext();
                }
                infoList.setValue(townInfos);
            }
            db.getReadableDb().setTransactionSuccessful();
            db.getReadableDb().endTransaction();
            Log.e(TAG, "Promo entry has been updated");
        } catch (Exception e){
            e.printStackTrace();
        }
        return infoList;
    }

    public LiveData<List<TownInfo>> getTownInfo(){
        MutableLiveData<List<TownInfo>> infoList = new MutableLiveData<>();
        try{
            db.getReadableDb().beginTransaction();
            String lsSql = "SELECT" +
                    "  a.sTownIDxx" +
                    ", a.sTownName" +
                    ", b.sProvName" +
                    ", a.sZippCode" +
                    ", b.sProvIDxx" +
                    " FROM CreditApp_Town a" +
                    ", CreditApp_Province b" +
                    " WHERE a.sProvIDxx = b.sProvIDxx" +
                    " AND a.cRecdStat = '1'" +
                    " AND b.cRecdStat = '1'";
            Cursor cursor = db.getReadableDb().rawQuery(lsSql, null);
            if(cursor.getCount() > 0){
                cursor.moveToFirst();
                List<TownInfo> townInfos = new ArrayList<>();
                for(int x = 0; x < cursor.getCount(); x++){
                    TownInfo info = new TownInfo(cursor.getString(0),
                            cursor.getString(1),
                            cursor.getString(4),
                            cursor.getString(2),
                            cursor.getString(3));
                    townInfos.add(info);
                    cursor.moveToNext();
                }
                infoList.setValue(townInfos);
            }
            db.getReadableDb().setTransactionSuccessful();
            db.getReadableDb().endTransaction();
            Log.e(TAG, "Promo entry has been updated");
        } catch (Exception e){
            e.printStackTrace();
        }
        return infoList;
    }
}
