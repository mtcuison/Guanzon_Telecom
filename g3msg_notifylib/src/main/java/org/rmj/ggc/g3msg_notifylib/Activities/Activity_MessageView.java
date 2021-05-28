package org.rmj.ggc.g3msg_notifylib.Activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import org.json.JSONObject;
import org.rmj.g3appdriver.dev.AppData;
import org.rmj.g3appdriver.etc.SharedPref;
import org.rmj.ggc.g3msg_notifylib.DbHelper.Get_NotificationData;
import org.rmj.ggc.g3msg_notifylib.Fragments.Fragment_GapMessageView;
import org.rmj.ggc.g3msg_notifylib.Fragments.Fragment_NotificationList;
import org.rmj.ggc.g3msg_notifylib.Fragments.Fragment_SysMessageView;
import org.rmj.ggc.g3msg_notifylib.Model.NotificationMessageModel;
import org.rmj.ggc.g3msg_notifylib.R;
import org.rmj.ggc.g3msg_notifylib.Sender.G3_ResponseSender;

import java.util.ArrayList;

public class Activity_MessageView extends AppCompatActivity {
    private static Activity_MessageView instance;
    private NotificationMessageModel poMsgModel;
    private ViewPager viewPager;
    public static Activity_MessageView getInstance(){
        return instance;
    }

    public NotificationMessageModel getPoMsgModel(){
        return poMsgModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_notification_view);
        poMsgModel = (NotificationMessageModel) getIntent().getSerializableExtra("notification");
        Toolbar toolbar = findViewById(R.id.toolbar_notificationView);
        viewPager = findViewById(R.id.viewpager_notification);
        updateMessageData();
        toolbar.setTitle(poMsgModel.getMessageTitle());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void updateMessageData(){
        G3_ResponseSender loResponse = new G3_ResponseSender(Activity_MessageView.this);
        String mesgid = poMsgModel.getMessageID();
        String userid = AppData.getInstance(Activity_MessageView.this).getUserID();
        if(!new Get_NotificationData().isMessageUnread(Activity_MessageView.this, mesgid, userid)) {
            loResponse.sendResponse(new G3_ResponseSender.onSendResponseListener() {
                @Override
                public JSONObject setResponseType() {
                    G3_ResponseSender.RESPONSE loJsonResponse = new G3_ResponseSender.RESPONSE();
                    return loJsonResponse.READ_RESPONSE(poMsgModel.getMessageID());
                }

                @Override
                public void onSendSuccessResult() {
                    new Fragment_NotificationList().getInstance().refreshView();
                }

                @Override
                public void onSendErrorResult(String error) {

                }
            });
        }
        viewPager.setAdapter(new NotificationViewAdapter(getSupportFragmentManager(), getFragmentView()));
    }

    static class NotificationViewAdapter extends FragmentStatePagerAdapter{

        private ArrayList<Fragment> fragmentlist = new ArrayList<>();

        NotificationViewAdapter(@NonNull FragmentManager fm, Fragment fragment) {
            super(fm);
            this.fragmentlist.add(fragment);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentlist.get(position);
        }

        @Override
        public int getCount() {
            return fragmentlist.size();
        }
    }

    private Fragment getFragmentView(){
        String ProductID = new SharedPref(Activity_MessageView.this).ProducID();
        switch (ProductID.toLowerCase()){
            case "guanzonapp":
                return new Fragment_GapMessageView();
            case "telecom":
            case "integsys":
                return new Fragment_SysMessageView();
        }
        return null;
    }
}
