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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONObject;
import org.rmj.g3appdriver.lib.telecom.Knox;
import org.rmj.ggc.samsung_knox.Etc.ClipBoardCopy;
import org.rmj.ggc.samsung_knox.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_KnoxOfflinePIN extends Fragment {
    private static final String TAG = Fragment_KnoxOfflinePIN.class.getSimpleName();

    private TextInputLayout tilImei;
    private TextInputEditText tieImei;
    private TextInputEditText tiePKey;
    private EditText txtKnoxPin;
    private MaterialButton btnGetPin;
    private ImageButton btnCopy;
    private Knox knox;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder builder;
    private Toast toast;

    public Fragment_KnoxOfflinePIN() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_knox_offline_pin, container, false);

        setupJavClassess();
        tilImei = view.findViewById(R.id.til_knoxImei);
        tieImei = view.findViewById(R.id.txt_knoxImei);
        tiePKey = view.findViewById(R.id.txt_knoxPasscode);
        txtKnoxPin = view.findViewById(R.id.txt_KnoxPIN);
        btnGetPin = view.findViewById(R.id.btn_knoxGetPIN);
        btnCopy = view.findViewById(R.id.btn_CopyToClipboard);

        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String KnoxPin = txtKnoxPin.getText().toString();
                String message;
                if (!KnoxPin.isEmpty()) {
                    new ClipBoardCopy(getActivity()).CopyTextClip("Knox_Pin", KnoxPin);
                    message = "Knox pin copied to clipboard.";
                } else {
                    message = "Unable to copy empty content.";
                }
                toast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
        btnGetPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!tieImei.getText().toString().isEmpty()){
                    new SendGetPIN(tieImei.getText().toString(), tiePKey.getText().toString()).execute();
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
        String passKey;
        JSONObject params;
        String message;

        SendGetPIN(String IMEI, String Passkey){
            this.deviceIMEI = IMEI;
            this.passKey = Passkey;
        }

        @Override
        protected void onPreExecute() {
            params = new JSONObject();
            try{
                params.put("deviceUid", deviceIMEI);
                params.put("challenge", passKey);
            } catch (Exception e){
                e.printStackTrace();
            }

            progressDialog.setTitle("Knox");
            progressDialog.setMessage("Sending request. Please wait...");
            progressDialog.setIcon(R.drawable.ic_guanzon_logo);
            progressDialog.setCancelable(false);
            showDialog();
            super.onPreExecute();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Integer... integers) {
            final String[] result = new String[1];
            knox.sendKnoxRequest(getActivity(), params.toString(), Knox.KnoxRequest.OFFLINE_PIN_REQUEST, new Knox.onKnoxRequestListener() {
                @Override
                public void onSuccessResult(String Result) {
                    result[0] = "success";
                    try{
                        JSONObject jsonObject = new JSONObject(Result);
                        JSONArray jsonArray = jsonObject.getJSONArray("pinNumber");
                        message = jsonArray.getString(0);
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
            if(s.equalsIgnoreCase("success")){
                txtKnoxPin.setText(message);
            } else {
                showNoticeDialog(message);
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
}
