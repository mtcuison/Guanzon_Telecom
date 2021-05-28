package org.rmj.ggc.telecom.Activity;

import android.content.Context;
import android.content.Intent;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.rmj.ggc.telecom.R;

import org.rmj.androidprojects.guanzongroup.g3creditapp.Local.AssetImportUpdate.Import_Branches;
import org.rmj.androidprojects.guanzongroup.g3creditapp.Local.AssetImportUpdate.OnImportAssetListener;
import org.rmj.g3appdriver.dev.AppData;
import org.rmj.g3appdriver.dev.Telephony;
import org.rmj.g3appdriver.etc.SessionManager;
import org.rmj.ggc.g3msg_notifylib.DbHelper.Get_NotificationData;
import org.rmj.ggc.g3msg_notifylib.Fragments.Fragment_NotificationList;
import org.rmj.ggc.telecom.Fragment.fragment_MainMenu;
import org.rmj.ggc.telecom.JavaExtras.ExitDialog;
import org.rmj.ggc.telecom.JavaExtras.SharedPref;

import java.util.ArrayList;
import java.util.Objects;

public class Activity_Dashboard extends AppCompatActivity {
    private static final String TAG = Activity_Dashboard.class.getSimpleName();
    private static Activity_Dashboard instance;

    SharedPref sharedPref;
    AppData db;
    Telephony getDeviceID;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private SessionManager sessionHandler;
    private BadgeDrawable notificationBadge;

    private int[] tabIconsToggled = {
            R.drawable.icon_dashboard_toggled,
            R.drawable.ic_notification_toggled};

    private int[] tabIcons = {
            R.drawable.ic_dashboard,
            R.drawable.ic_notifications};

    public static Activity_Dashboard getInstance(){
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_main);

        new ImportBranchTask(Activity_Dashboard.this).execute();
        setupClassess();
        checkLogin();
        setupWidgets();
        setupViewPager();
        setupTablayout();
        checkLogin();
    }

    private void setupClassess(){
        sessionHandler = new SessionManager(getApplicationContext());
        sharedPref = new SharedPref(getApplicationContext());
        getDeviceID = new Telephony(Activity_Dashboard.this);
        db = AppData.getInstance(Activity_Dashboard.this);
    }

    private void setupWidgets(){
        Toolbar toolbar = findViewById(R.id.toolbar_tcm_dashboard);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setIcon(R.drawable.ic_guanzon_logo);
        tabLayout = findViewById(R.id.tabs_dashboard);
        viewPager = findViewById(R.id.viewpager_dashboard);
    }
    private void setupTablayout(){
        tabLayout.setupWithViewPager(viewPager);
        notificationBadge = Objects.requireNonNull(tabLayout.getTabAt(1)).getOrCreateBadge();
        int count = new Get_NotificationData().getUnreadNotificationCount(Activity_Dashboard.this, db.getUserID());
        notificationBadge.setNumber(count);
        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(tabIconsToggled[0]);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(tabIcons[1]);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Objects.requireNonNull(tabLayout.getTabAt(tab.getPosition())).setIcon(tabIconsToggled[tab.getPosition()]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Objects.requireNonNull(tabLayout.getTabAt(tab.getPosition())).setIcon(tabIcons[tab.getPosition()]);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setupViewPager(){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.add(new fragment_MainMenu());
        adapter.add(new Fragment_NotificationList());
        viewPager.setAdapter(adapter);
    }

    private void checkLogin(){
        if(!sessionHandler.isLoggedIn()){
            Intent intent = new Intent(Activity_Dashboard.this, Activity_Splash.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        OpenExitDialog();
    }

    private void OpenExitDialog(){
        ExitDialog exitDialog = new ExitDialog();
        exitDialog.show(getSupportFragmentManager(),"Application Exit");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater loInflater = getMenuInflater();
        loInflater.inflate(R.menu.dashboard_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_logout:
                sessionHandler.setLogin(false);
                startActivity(new Intent(Activity_Dashboard.this, Activity_Splash.class));
                finish();

            case R.id.action_exit:
                System.exit(1);
        }
        return super.onOptionsItemSelected(item);
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter{

        private final ArrayList<Fragment> mFragmentList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return mFragmentList.get(i);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void add(Fragment fragment){
            mFragmentList.add(fragment);
        }
    }

    public void setNotificationBadge(){
        int count = new Get_NotificationData().getUnreadNotificationCount(Activity_Dashboard.this, db.getUserID());
        notificationBadge.setNumber(count);
        notificationBadge.setVisible(getBadgeVisibility(count));
    }

    private boolean getBadgeVisibility(int count){
        return count != 0;
    }

    private static class ImportBranchTask extends AsyncTask<Void, Void, String>{
        private Context context;

        public ImportBranchTask(Context context){
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String[] lsResult = {""};
            new Import_Branches().SendImportRequest(context, new OnImportAssetListener() {
                @Override
                public void onImportSuccess() {
                    lsResult[0] = "success";
                }

                @Override
                public void onImportFailed(String Message) {
                    lsResult[0] = "success";
                }
            });
            return lsResult[0];
        }
    }
}
