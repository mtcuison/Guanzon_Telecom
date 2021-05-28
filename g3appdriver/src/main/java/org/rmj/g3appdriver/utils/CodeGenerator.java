package org.rmj.g3appdriver.utils;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;

public class CodeGenerator {

    private String EncryptionKEY = "20190625";
    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
    MySQLAESCrypt encryptionManager = new MySQLAESCrypt();
    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
    static String EncryptedQrCode = "";
    static String scanType = "";


    public void setEncryptedQrCode(String encryptedQrCode){
        EncryptedQrCode = encryptedQrCode;
    }

    public void setScanType(String ScanType){
        scanType = ScanType;
    }

    public String getScanType(){
        return scanType;
    }

    /**
     * Generate QRCODE
     * Encrypt the String value of
     * SOURCE    this must be REDEEM/PREORDER...
     * DeviceIMEI
     * GCardNumber
     * UserID
     * MobileNumber
     * DateTime            DateTime Format must be(YYYYMMDDHHMMSS)
     * AvailablePoints
     * Transanox = NULL
     * TransNox value is null until a successful redemption of points*/
    public Bitmap generateQrCode(String SOURCE, String DeviceImei, String CardNumber, String UserID, String MobileNumber, String DateTime, double AvailablePoints, String sModelCde, String TransNox){
        Bitmap bitmap = null;
        String UnEncryptedString = SOURCE + "»" + DeviceImei + "»" + CardNumber + "»" + UserID + "»" + MobileNumber + "»" + DateTime + "»" + AvailablePoints + "»" + sModelCde + "»" + TransNox;
        String EncryptedCode = MySQLAESCrypt.Encrypt(UnEncryptedString, EncryptionKEY);
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(EncryptedCode, BarcodeFormat.QR_CODE, 900, 900);
            bitmap = barcodeEncoder.createBitmap(bitMatrix);
            return bitmap;
        }catch(Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }

    public Bitmap generateGCardCodex(String SOURCE,
                                     String DeviceImei,
                                     String CardNumber,
                                     String UserID,
                                     String MobileNumber,
                                     String DateTime,
                                     double AvailablePoints,
                                     String sModelCde,
                                     String TransNox){
        Bitmap GcardCodex = null;
        String UnEncryptedString = SOURCE + "»" + DeviceImei + "»" + CardNumber + "»" + UserID + "»" + MobileNumber + "»" + DateTime + "»" + AvailablePoints + "»" + sModelCde + "»" + TransNox;
        String EncryptedCode = MySQLAESCrypt.Encrypt(UnEncryptedString, EncryptionKEY);
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(EncryptedCode, BarcodeFormat.QR_CODE, 700, 700);
            GcardCodex = barcodeEncoder.createBitmap(bitMatrix);
            return GcardCodex;
        }catch(Exception e){
            e.printStackTrace();
            return GcardCodex;
        }
    }

    public String generateSecureNo(String SecureNo){
        return MySQLAESCrypt.Encrypt(SecureNo, EncryptionKEY);
    }

    /***********************************************************
     * QrCode is decrypted to get the original value
     * */
    private String decryptedQrCodeValue(){
        String decyptedQrCode = MySQLAESCrypt.Decrypt(EncryptedQrCode, EncryptionKEY);
        return decyptedQrCode;
    }


    /***********************************************************
     * QrCode Decrypted value is split into String array[]
     * index 0 = Source
     * index 1 = PIN
     * index 2 = sTransNox
     * index 3 = dTransact
     * index 4 = sSourceNo
     * index 5 = sSourceCD
     * index 6 = nPointsxx
     * index 7 = sOTPasswd
     * index 8 = sIMEINoxx
     * index 9 = sUserIDxx
     * index 10 = sCardNumber
     * index 11 = sMobileNo
     * */
    private String getKeyValueOf(String decryptedCode, int ArrayIndexPosition){
        String returnValue = "";
        ArrayList CodeValue = new ArrayList();
        try {
            String[] frstValue = decryptedCode.split(";");
            String[] scndValue;
            String[] thrdValue;
            if (frstValue.length >= 0) {
                CodeValue.clear();
                for (int sTransIndex = 0; sTransIndex < frstValue.length; sTransIndex++) {
                    scndValue = frstValue[0].split("»");
                    for (int x = 0; x < scndValue.length; x++) {
                        if (x == 6) {
                            thrdValue = scndValue[6].split("@");
                            for (int i = 0; i < thrdValue.length; i++) {
                                CodeValue.add(thrdValue[i]);
                            }
                        } else {
                            CodeValue.add(scndValue[x]);
                        }
                    }
                }
            }
            for (int index = 0; index < CodeValue.size(); index++) {
                returnValue = String.valueOf(CodeValue.get(ArrayIndexPosition));
            }
        } catch (Exception e){
            e.printStackTrace();
            returnValue = "INVALID";
        }
        return  returnValue;
    }

    /**
     * Get Transaction Source here...
     * Transaction Source is either ONLINE || OFFLINE
     *
     * */
    public String getTransSource(){
        String returnValue;
        try {
            String decryptedQrCode = decryptedQrCodeValue();
            returnValue = getKeyValueOf(decryptedQrCode, 0);
        } catch (Exception e){
            e.printStackTrace();
            returnValue = null;
        }
        return returnValue;
    }

    /**
     * Get QrCode Decrypted value
     * Transaction PIN is inside the array with @ splitter
     * Transaction PIN is inside Array Index[7]
     *
     * DO NOT DISPLAY TRANSACTION PIN IF VALIDATIONS ARE NOT MET...
     * */
    public String getTransactionPIN(){
        String decryptedQrCode = decryptedQrCodeValue();
            return getKeyValueOf(decryptedQrCode,1);
    }

    /**
     * Get Device IMEI for Device Barcode Scanning
     * For Transaction Validation
     * If IMEI in the QRCode != current Device IMEI
     * Transaction is Unsuccessful/INVALID
     *
     *  Get current USERID for Validation
     *  Get current GCARD Number for Validation
     *  Get current MOBILE NUMBER for Validation
     *  All Details for validation must be met by current device scanning the QRCode...
     *  */
    public String getTransDevImei(){
        String decryptedQrCode = decryptedQrCodeValue();
        return getKeyValueOf(decryptedQrCode,2).replaceAll("[^\\\\x20-\\\\x7e]", "");
    }
    public String getUserID(){
        String decryptedQrCode = decryptedQrCodeValue();
        return getKeyValueOf(decryptedQrCode, 3).replaceAll("[^\\\\x20-\\\\x7e]", "");
    }
    public String getGCardNumber(){
        String decryptedQrCode = decryptedQrCodeValue();
        return getKeyValueOf(decryptedQrCode,4).replaceAll("[^\\\\x20-\\\\x7e]", "");
    }
    public String getMobileNumber(){
        String decryptedQrCode = decryptedQrCodeValue();
            return getKeyValueOf(decryptedQrCode,5);
    }

    public String getTransNox(){
        String decryptedQrCode = decryptedQrCodeValue();
            return getKeyValueOf(decryptedQrCode, 6);
    }
    public String getDTransact(){
        String decryptedQrCode = decryptedQrCodeValue();
            return getKeyValueOf(decryptedQrCode, 7);
    }
    public String getSourceNo(){
            String decryptedQrCode = decryptedQrCodeValue();
            return getKeyValueOf(decryptedQrCode, 8);
    }
    public String getSourceCD(){
            String decryptedQrCode = decryptedQrCodeValue();
            return getKeyValueOf(decryptedQrCode, 9);
    }
    public String getPointsxx(){
        String decryptedQrCode = decryptedQrCodeValue();
            return getKeyValueOf(decryptedQrCode, 10);
    }
    /**If Conditions above is not met
     *Display an INVALID TRANSACTION...
     *
     *
     * */


    /**
     * Encryption Functions below are use to encrypt all points in local database
     *
     *
     * */
    public String encryptPointsxx(double sPointsxx){
        return MySQLAESCrypt.Encrypt(String.valueOf(Double.valueOf(sPointsxx)), EncryptionKEY);
    }

    public String decryptPointsxx(String encryptedPointsxx){
        return MySQLAESCrypt.Decrypt(encryptedPointsxx, EncryptionKEY);
    }

    public String generateTransNox(){
        String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int count = 12;
        StringBuilder builder = new StringBuilder();
            while (count-- != 0) {
                int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
                builder.append(ALPHA_NUMERIC_STRING.charAt(character));
            }
        return builder.toString();
    }

    public boolean isCodeValid(){
        return !getTransSource().equalsIgnoreCase("INVALID");
    }

    public boolean isDeviceValid(String DeviceImei, String UserID, String GCardNumber) {
        String gcard = getGCardNumber();
        String Imei = getTransDevImei();
        String user = getUserID();
        return gcard.equalsIgnoreCase(GCardNumber) &&
                Imei.equalsIgnoreCase(DeviceImei) &&
                user.equalsIgnoreCase(UserID);
    }

    public boolean isQrCodeApplication(){
        return getTransSource().equalsIgnoreCase("APPLICATION");
}

    public boolean isQrCodeTransaction(){
        return getTransSource().equalsIgnoreCase("PREORDER") ||
                getTransSource().equalsIgnoreCase("REDEMPTION") ||
                getTransSource().equalsIgnoreCase("ONLINE") ||
                getTransSource().equalsIgnoreCase("OFFLINE");
    }

    public boolean isTransactionVoid(){
        double points = Double.parseDouble(getPointsxx());
        return points < 0;
    }
}
