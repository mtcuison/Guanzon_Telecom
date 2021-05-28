package org.rmj.ggc.telecom.DataAdapters.CashCountData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rmj.ggc.telecom.R;
import java.util.ArrayList;

public class Adapter_LocalCashCount extends BaseAdapter {

    private Context context;
    private ArrayList<String> LogDate = new ArrayList<String>();
    private ArrayList<String> LogUser = new ArrayList<String>();
    private ArrayList<String> LogTime = new ArrayList<String>();

    public Adapter_LocalCashCount(Context context, ArrayList<String> logDate, ArrayList<String> logUser, ArrayList<String> logTime) {
        this.context = context;
        LogDate = logDate;
        LogUser = logUser;
        LogTime = logTime;
        this.notifyDataSetChanged();
    }

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DataHolder dataHolder = new DataHolder();
        LayoutInflater inflater;

        if(convertView == null){
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_local_cashcount, null);
            dataHolder.lblLogDate = convertView.findViewById(R.id.lbl_LogDate);
            dataHolder.lblLogUser = convertView.findViewById(R.id.lbl_LogUser);
            dataHolder.lblLogTime = convertView.findViewById(R.id.lbl_LogTime);
            dataHolder.IMGStatus = convertView.findViewById(R.id.Img_sync_status);

        } else{
            dataHolder = (DataHolder)convertView.getTag();
        }
        dataHolder.lblLogDate.setText(LogDate.get(position));
        dataHolder.lblLogUser.setText(LogUser.get(position));
        dataHolder.lblLogTime.setText(LogTime.get(position));
        dataHolder.IMGStatus.setImageResource(R.drawable.ic_local_database);
        convertView.setTag(dataHolder);
        return convertView;
    }

    private class DataHolder{
        TextView lblLogDate;
        TextView lblLogUser;
        TextView lblLogTime;
        ImageView IMGStatus;
    }
}
