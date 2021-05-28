package org.rmj.ggc.telecom.JavaExtras;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDialogFragment;

import org.rmj.ggc.telecom.Activity.Activity_Splash;

public class SessionExpireDialog extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Session Expired!")
                .setMessage("Last login session is already expired please. Log in again to Continue")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SessionHandler sessionHandler = new SessionHandler(getActivity());
                sessionHandler.setLogin(false);
                getActivity().finish();
                startActivity(new Intent(getActivity().getApplicationContext(), Activity_Splash.class));
            }
        });
        return builder.create();
    }
}
