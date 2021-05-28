package org.rmj.ggc.telecom.JavaExtras;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDialogFragment;

import org.rmj.g3appdriver.dev.AppData;
import org.rmj.ggc.telecom.Activity.Activity_Splash;

public class LogoutDialog extends AppCompatDialogFragment {

    AppData sql_statements;
    SharedPref sharedPref;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        sql_statements = AppData.getInstance(getActivity().getApplicationContext());
        sharedPref = new SharedPref(getActivity().getApplicationContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Logout User")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SessionHandler sessionHandler = new SessionHandler(getActivity());
                        clearSharedPref();
                        sessionHandler.setLogin(false);

                        startActivity(new Intent(getActivity().getApplicationContext(), Activity_Splash.class));
                        getActivity().finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }

    private void clearSharedPref(){
        sharedPref.setTemp_DeviceID("");
        sharedPref.setTemp_ProductID("");
        sharedPref.setTemp_DateLogin("");
        sharedPref.setTemp_sessionExp("");
    }
}
