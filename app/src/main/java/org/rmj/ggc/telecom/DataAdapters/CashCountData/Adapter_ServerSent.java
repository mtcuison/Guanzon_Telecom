package org.rmj.ggc.telecom.DataAdapters.CashCountData;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rmj.ggc.telecom.R;

import java.util.ArrayList;

public class Adapter_ServerSent extends BaseAdapter {

    private Context context;
    private ArrayList<String> LogDate = new ArrayList<String>();
    private ArrayList<String> TransDate = new ArrayList<String>();
    private ArrayList<String> LogRecvd = new ArrayList<String>();
    private ArrayList<String> LogReqID = new ArrayList<String>();

    public Adapter_ServerSent(Context context, ArrayList<String> logDate, ArrayList<String> logReqNM, ArrayList<String> transDate, ArrayList<String> logRecvd, ArrayList<String> logReqID) {
        this.context = context;
        LogDate = logDate;
        TransDate = transDate;
        LogRecvd = logRecvd;
        LogReqID = logReqID;
        LogReqNM = logReqNM;
    }

    private ArrayList<String> LogReqNM = new ArrayList<String>();

    @Override
    public int getCount() {
        return this.LogDate.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DataHolder dHolder = new DataHolder();
        LayoutInflater inflater;

        if(convertView == null){
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_server_cashcount, null);
            dHolder.lblTransDate = convertView.findViewById(R.id.lblLog_TransDate);
            dHolder.lblLogReqNM = convertView.findViewById(R.id.lblLogReqNM);
            dHolder.lblReqName = convertView.findViewById(R.id.lblReqName);
            dHolder.IMGStatus = convertView.findViewById(R.id.img_serverSync);
        } else{
            dHolder = (DataHolder) convertView.getTag();
        }

        dHolder.lblTransDate.setText(TransDate.get(position));
        dHolder.lblLogReqNM.setText(LogReqNM.get(position));
        dHolder.lblReqName.setText(LogReqID.get(position));
        dHolder.IMGStatus.setImageResource(R.drawable.ic_save_to_server);
        convertView.setTag(dHolder);
        return convertView;
    }

    private class DataHolder{
        TextView lblTransDate;
        TextView lblLogReqNM;
        TextView lblReqName;
        ImageView IMGStatus;
    }
}
