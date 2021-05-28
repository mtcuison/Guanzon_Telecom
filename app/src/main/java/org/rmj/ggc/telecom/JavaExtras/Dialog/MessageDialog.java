package org.rmj.ggc.telecom.JavaExtras.Dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rmj.ggc.telecom.R;

public class MessageDialog {
    private Context mContext;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflater;
    private onClickListener mListener;

    private String title;
    private String message;
    private String dialogType;
    private int smallIcon;
    private int largeIcon;
    private boolean cancelable = true;
    private View view;

    private TextView lblTitle;
    private TextView lblMessage;
    private Button btnPositive;
    private Button btnNegative;
    private ImageView imgType;

    public MessageDialog(Context context){
        this.mContext = context;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSmallIcon(int smallIcon) {
        this.smallIcon = smallIcon;
    }

    public void setLargeIcon(int largeIcon) {
        this.largeIcon = largeIcon;
    }

    public void setView(View view) {
        this.view = view;
    }

    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    public void setPositiveButton(String Text, final onClickListener listener){
        btnPositive.setVisibility(View.VISIBLE);
        btnPositive.setText(Text);
        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                   listener.onClickListener(v);
                }
            }
        });
    }

    public void setNegativeButton(String Text, final onClickListener listener){
        btnNegative.setVisibility(View.VISIBLE);
        btnNegative.setText(Text);
        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onClickListener(v);
                }
            }
        });
    }

    public void createDialog(){
        builder = new AlertDialog.Builder(mContext);
        inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.dialog_message, null);
        builder.setCancelable(cancelable)
                .setView(view);
        dialog = builder.create();
        dialog.setCancelable(false);

        lblMessage = view.findViewById(R.id.lbl_dialogMessage);
        lblTitle = view.findViewById(R.id.lbl_dialogTitle);
        btnPositive = view.findViewById(R.id.btn_dialogPositive);
        btnNegative = view.findViewById(R.id.btn_dialogNegative);
        imgType = view.findViewById(R.id.img_dialogType);
        lblTitle.setText(title);
        lblMessage.setText(message);
        imgType.setImageResource(largeIcon);
    }

    public void showDialog(){
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_windows);
        dialog.show();
    }

    public interface onClickListener{
        void onClickListener(View view);
    }

    public void dismissDialog(){
        dialog.dismiss();
    }

    public void setMessageType(String Type){
        switch (Type){
            case "error":
                break;
        }
    }
}
