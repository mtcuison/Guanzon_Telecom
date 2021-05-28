package org.rmj.ggc.g3msg_notifylib.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import org.rmj.ggc.g3msg_notifylib.Activities.Activity_MessageView;
import org.rmj.ggc.g3msg_notifylib.Model.NotificationMessageModel;
import org.rmj.ggc.g3msg_notifylib.R;
import org.rmj.ggc.g3msg_notifylib.Utils.FormatUIText;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_SysMessageView extends Fragment {
    private final static String TAG = Fragment_SysMessageView.class.getSimpleName();
    private NotificationMessageModel poMsgModel;

    private TextView lblTitle;
    private TextView lblSender;
    private TextView lblDateTime;
    private TextView lblMessage;
    private MaterialButton btnReply;
    private MaterialButton btnCshCt;
    public Fragment_SysMessageView() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sys_message_view, container, false);
        setupWidgets(view);
        setupData();
        return view;
    }

    private void setupWidgets(View v){
        lblSender = v.findViewById(R.id.lbl_ntf_messageSender);
        lblDateTime = v.findViewById(R.id.lbl_ntf_messageDateTime);
        lblMessage = v.findViewById(R.id.lbl_ntf_messageBody);
        btnReply = v.findViewById(R.id.btn_Reply);
        btnCshCt = v.findViewById(R.id.btn_cashCount);
    }

    private void setupData(){
        poMsgModel = new Activity_MessageView().getInstance().getPoMsgModel();
        lblSender.setText(poMsgModel.getMessageSender());
        lblDateTime.setText(new FormatUIText().getParseDateTime(poMsgModel.getMessageDate()));
        lblMessage.setText(poMsgModel.getMessageBody());
        btnReply.setOnClickListener(new OnButtonClickListener());
        btnCshCt.setOnClickListener(new OnButtonClickListener());
    }

    class OnButtonClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if(id == R.id.btn_Reply){
                Toast.makeText(getActivity(), "This feature is not available for now.", Toast.LENGTH_SHORT).show();
            } else if(id == R.id.btn_cashCount){

            }
        }
    }
}
