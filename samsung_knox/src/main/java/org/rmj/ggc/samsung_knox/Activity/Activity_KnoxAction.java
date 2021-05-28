package org.rmj.ggc.samsung_knox.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;

import org.rmj.ggc.samsung_knox.Fragments.Fragment_GetPIN;
import org.rmj.ggc.samsung_knox.Fragments.Fragment_KnoxActivate;
import org.rmj.ggc.samsung_knox.Fragments.Fragment_KnoxOfflinePIN;
import org.rmj.ggc.samsung_knox.Fragments.Fragment_KnoxStatus;
import org.rmj.ggc.samsung_knox.Fragments.Fragment_KnoxUpload;
import org.rmj.ggc.samsung_knox.R;

import java.util.ArrayList;

public class Activity_KnoxAction extends AppCompatActivity {
    private static Activity_KnoxAction instance;

    private String title;
    private int FragmentType;

    public static Activity_KnoxAction getInstance(){
        return instance;
    }

    public int getFragmentType(){
        return FragmentType;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_knox_action);
        ViewPager viewPager = findViewById(R.id.viewpager_knox);
        Toolbar toolbar = findViewById(R.id.toolbar_knoxRegistration);
        KnoxFragmentAdapter adapter = new KnoxFragmentAdapter(getSupportFragmentManager());
        FragmentType = getIntent().getIntExtra("knox", 0);
        adapter.addFragment(getFragment(FragmentType), title);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    static class KnoxFragmentAdapter extends FragmentStatePagerAdapter {

        ArrayList<Fragment> fragmentList = new ArrayList<>();
        ArrayList<String> fragmentTitle = new ArrayList<>();

        KnoxFragmentAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        void addFragment(Fragment fragment, String Title){
            fragmentList.add(fragment);
            fragmentTitle.add(Title);
        }
    }

    private Fragment getFragment(int FragmentType){
        switch (FragmentType){
            case 0:
                title = "Upload Device";
                return new Fragment_KnoxUpload();
            case 1:
                title = "Activate Device";
                return new Fragment_KnoxActivate();
            case 2:
                title = "Get Offline PIN";
                return new Fragment_KnoxOfflinePIN();
            case 3:
                title = "Get PIN";
                return new Fragment_GetPIN();
            case 4:
                title = "Check Device";
                return new Fragment_KnoxStatus();
            case 5:
                title = "Unlock Device";
                return new Fragment_GetPIN();
        }
        return null;
    }
}
