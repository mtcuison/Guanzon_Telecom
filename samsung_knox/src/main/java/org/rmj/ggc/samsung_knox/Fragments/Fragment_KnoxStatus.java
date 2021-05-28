package org.rmj.ggc.samsung_knox.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONObject;
import org.rmj.g3appdriver.lib.telecom.Knox;
import org.rmj.ggc.samsung_knox.R;

import java.text.DateFormatSymbols;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_KnoxStatus extends Fragment {
    private static final String TAG = Fragment_KnoxOfflinePIN.class.getSimpleName();

    private TextInputLayout tilImei;
    private TextInputEditText tieImei;
    private TextView lblDeviceImei, lblStatus, lblDetail, lblDate;
    private Knox knox;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder builder;
    private Toast toast;

    public Fragment_KnoxStatus() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_knox_status, container, false);

        setupJavClassess();
        tilImei = view.findViewById(R.id.til_knoxImei);
        tieImei = view.findViewById(R.id.txt_knoxImei);
        lblDeviceImei = view.findViewById(R.id.lbl_knoxDeviceIMEI);
        lblStatus = view.findViewById(R.id.lbl_knoxDeviceStatus);
        lblDetail = view.findViewById(R.id.lbl_knoxDetailStatus);
        lblDate = view.findViewById(R.id.lbl_knoxStatusDate);
        MaterialButton btnCheckStatus = view.findViewById(R.id.btn_knoxCheckStatus);

        btnCheckStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!tieImei.getText().toString().isEmpty()){
                    new SendGetPIN(tieImei.getText().toString()).execute();
                } else {
                    toast = Toast.makeText(getActivity(), "Please enter device IMEI", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });

        return view;
    }


    private void setupJavClassess(){
        progressDialog = new ProgressDialog(getActivity());
        knox = new Knox();
        builder = new AlertDialog.Builder(getActivity());
    }

    @SuppressLint("StaticFieldLeak")
    class SendGetPIN extends AsyncTask<Integer, Integer, String> {

        String deviceIMEI;
        JSONObject params;
        JSONObject Json;
        String message;

        SendGetPIN(String IMEI){
            this.deviceIMEI = IMEI;
        }

        @Override
        protected void onPreExecute() {
            params = new JSONObject();
            try{
                params.put("deviceUid", deviceIMEI);
                params.put("pageNum", 0);
                params.put("pageSize", 30);
            } catch (Exception e){
                e.printStackTrace();
            }

            progressDialog.setTitle("Samsung Knox");
            progressDialog.setMessage("Checking device status. Please wait...");
            progressDialog.setIcon(R.drawable.ic_guanzon_logo);
            progressDialog.setCancelable(false);
            showDialog();
            super.onPreExecute();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Integer... integers) {
            final String[] result = new String[1];
            knox.sendKnoxRequest(getActivity(), params.toString(), Knox.KnoxRequest.GET_DEVICE_LOG_REQUEST, new Knox.onKnoxRequestListener() {
                @Override
                public void onSuccessResult(String Result) {
                    result[0] = "success";
                    try{
                        long maxValue;
                        JSONObject jsonObject = new JSONObject(Result);
                        JSONArray jsonArray = jsonObject.getJSONArray("deviceLogs");
                        maxValue = (long)jsonArray.getJSONObject(jsonArray.length()-1).get("time");
                        for(int x = 0; x < jsonArray.length(); x++){
                            JSONObject loJson = new JSONObject(jsonArray.getString(x));

                            long CurrMax = (long)loJson.get("time");
                            if(CurrMax > maxValue){
                                maxValue = CurrMax;
                                Json = loJson;
                            }
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onErrorResult(String errorMessage) {
                    result[0] = "error";
                    message = errorMessage;
                }
            });
            return result[0];
        }

        @Override
        protected void onPostExecute(String s) {
            hideDialog();
            try {
                if (s.equalsIgnoreCase("success")) {
                    lblDeviceImei.setText(tieImei.getText().toString());
                    lblStatus.setText(Json.getString("deviceStatus"));
                    lblStatus.setTextColor(getTextColor(Json.getString("deviceStatus")));
                    lblDetail.setText(Json.getString("details"));
                    lblDate.setText(getReadableDateFormat((long)Json.get("time")));
                    tieImei.setText("");
                } else {
                    showNoticeDialog(message);
                }
                this.cancel(true);
            } catch (Exception e){
                e.printStackTrace();
            }
            super.onPostExecute(s);
        }
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }
    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void showNoticeDialog(String Message){
        builder.setMessage(Message);
        builder.setTitle("Samsung Knox");
        builder.setCancelable(false);
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private String getReadableDateFormat(long date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        int month = calendar.get(Calendar.MONTH);
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        return months[month] + " " + calendar.get(Calendar.DAY_OF_MONTH) + ", " + calendar.get(Calendar.YEAR);
    }

    private int getTextColor(String status){
        switch (status.toLowerCase()){
            case "active":
                return getActivity().getResources().getColor(R.color.Knox_Active);
            case "activating":
                return getActivity().getResources().getColor(R.color.Knox_Activating);
            case "lock":
                return getActivity().getResources().getColor(R.color.Knox_Lock);
            case "locking":
                return getActivity().getResources().getColor(R.color.Knox_Locking);
            case "pending":
                return getActivity().getResources().getColor(R.color.Knox_Pending);
        }
        return getActivity().getResources().getColor(R.color.textColorPrimaryDark);
    }
}
