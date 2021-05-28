package org.rmj.ggc.samsung_knox.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;
import org.rmj.g3appdriver.lib.telecom.Knox;
import org.rmj.ggc.samsung_knox.Activity.Activity_KnoxScanner;
import org.rmj.ggc.samsung_knox.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_KnoxActivate extends Fragment {
    private static final String TAG = Fragment_KnoxActivate.class.getSimpleName();
    private static Fragment_KnoxActivate instance;
    private Knox knox;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder builder;
    private Toast toast;

    private TextInputLayout tilImei;

    private TextInputEditText txtImeiCode, txtRemarks;

    public static Fragment_KnoxActivate getInstance(){
        return instance;
    }

    public Fragment_KnoxActivate() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        instance = this;
        View view = inflater.inflate(R.layout.fragment_knox_activate, container, false);
        setupJavClassess();
        setupWidgets(view);
        return view;
    }

    private void setupJavClassess(){
        progressDialog = new ProgressDialog(getActivity());
        knox = new Knox();
        builder = new AlertDialog.Builder(getActivity());
    }

    private void setupWidgets(View v){
        tilImei = v.findViewById(R.id.til_knoxImei);
        TextInputLayout tilRemarks = v.findViewById(R.id.til_knoxRemarks);

        ImageButton btnScanImei = v.findViewById(R.id.btn_knoxScan);
        Button btnSendImei = v.findViewById(R.id.btn_knoxRegister);
        txtImeiCode = v.findViewById(R.id.txt_knoxImei);
        txtRemarks = v.findViewById(R.id.txt_knoxRemarks);

        txtImeiCode.addTextChangedListener(new MyTextWatcher(tilImei));
        btnSendImei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isImeiInputValid()) {
                    new SendKnoxRequest().execute();
                }
            }
        });

        btnScanImei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Activity_KnoxScanner.class));
               /* Intent intent = new Intent(getActivity(), BarcodeScanner.class);
                //intent.setFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);*/
            }
        });
    }

    class SendKnoxRequest extends AsyncTask<Integer, Integer, String> {

        String err_Message;
        JSONObject params;

        @Override
        protected void onPreExecute() {
            params = new JSONObject();
            try {
                params.put("deviceUid", txtImeiCode.getText().toString());
                params.put("approveComment", txtRemarks.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.setTitle("Knox");
            progressDialog.setMessage("Activating Device. Please wait...");
            progressDialog.setIcon(R.drawable.ic_guanzon_logo);
            progressDialog.setCancelable(false);
            showDialog();
            super.onPreExecute();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Integer... integers) {
            final String[] result = new String[1];
            knox.sendKnoxRequest(getActivity(), params.toString(), Knox.KnoxRequest.ACTIVATE_REQUEST, new Knox.onKnoxRequestListener() {
                @Override
                public void onSuccessResult(String Result) {
                    result[0] = "success";
                }

                @Override
                public void onErrorResult(String errorMessage) {
                    result[0] = "error";
                    err_Message = errorMessage;
                }
            });
            return result[0];
        }

        @Override
        protected void onPostExecute(String s) {
            hideDialog();
            if(s.equalsIgnoreCase("success")){
                showNoticeDialog("Activated Successfully");
            } else {
                showNoticeDialog(err_Message);
            }
            this.cancel(true);
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

    /**
     * Validate the user input
     * */
    private boolean isImeiInputValid(){
        if(txtImeiCode.getText().toString().isEmpty()) {
            tilImei.setError("Please enter a device Imei.");
            return false;
        } else if(txtImeiCode.getText().length() != 15){
            tilImei.setError("Please make sure Imei is correct");
            return false;
        } else {
            tilImei.setErrorEnabled(false);
        }
        return true;
    }

    class MyTextWatcher implements TextWatcher {
        /* *
         * The set TextInputLayout view to TextWatcher Class
         * this automatically checks the input of user...*/
        private View mView;

        MyTextWatcher(View view){
            this.mView = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(mView.getId() == R.id.txt_knoxImei) {
                isImeiInputValid();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private void showNoticeDialog(String Message){
        builder.setMessage(Message);
        builder.setTitle("Samsung Knox");
        builder.setCancelable(false);
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void setImei(String IMEI){
        txtImeiCode.setText(IMEI);
    }
}
