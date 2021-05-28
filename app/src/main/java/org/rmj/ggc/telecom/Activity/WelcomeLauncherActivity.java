package org.rmj.ggc.telecom.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rmj.ggc.telecom.R;

import org.rmj.androidprojects.guanzongroup.g3creditapp.Local.AssetImportUpdate.ImportInstance;
import org.rmj.androidprojects.guanzongroup.g3creditapp.Local.AssetImportUpdate.Import_BarangayAsset;
import org.rmj.androidprojects.guanzongroup.g3creditapp.Local.AssetImportUpdate.Import_Branches;
import org.rmj.androidprojects.guanzongroup.g3creditapp.Local.AssetImportUpdate.Import_Brand;
import org.rmj.androidprojects.guanzongroup.g3creditapp.Local.AssetImportUpdate.Import_Category;
import org.rmj.androidprojects.guanzongroup.g3creditapp.Local.AssetImportUpdate.Import_CountryAsset;
import org.rmj.androidprojects.guanzongroup.g3creditapp.Local.AssetImportUpdate.Import_McModel;
import org.rmj.androidprojects.guanzongroup.g3creditapp.Local.AssetImportUpdate.Import_McModel_Price;
import org.rmj.androidprojects.guanzongroup.g3creditapp.Local.AssetImportUpdate.Import_Occupation;
import org.rmj.androidprojects.guanzongroup.g3creditapp.Local.AssetImportUpdate.Import_ProvinceAsset;
import org.rmj.androidprojects.guanzongroup.g3creditapp.Local.AssetImportUpdate.Import_Religion;
import org.rmj.androidprojects.guanzongroup.g3creditapp.Local.AssetImportUpdate.Import_TermCategoryAsset;
import org.rmj.androidprojects.guanzongroup.g3creditapp.Local.AssetImportUpdate.Import_TownAsset;
import org.rmj.androidprojects.guanzongroup.g3creditapp.Local.AssetImportUpdate.OnImportAssetListener;
import org.rmj.g3appdriver.etc.SessionManager;
import org.rmj.ggc.telecom.JavaExtras.SessionHandler;

public class WelcomeLauncherActivity extends AppCompatActivity {
    private static final String TAG = WelcomeLauncherActivity.class.getSimpleName();

    private ImportInstance[] instances = {
            new Import_BarangayAsset(),
            new Import_TownAsset(),
            new Import_ProvinceAsset(),
            new Import_CountryAsset(),
            new Import_McModel(),
            new Import_McModel_Price(),
            new Import_Brand(),
            new Import_Category(),
            new Import_TermCategoryAsset(),
            new Import_Religion(),
            new Import_Branches(),
            new Import_Occupation()};

    private ProgressBar progressBar;

    private ViewPager viewPager;
    private LinearLayout dotsLayout;
    private SessionHandler sessionHandler;
    private int[] layouts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_welcome_launcher);

        sessionHandler = new SessionHandler(getApplicationContext());
        progressBar = findViewById(R.id.progress_assetDowload);
        viewPager = findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.dots_layout);

        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.welcome_instruction1,
                R.layout.welcome_instruction2,
                R.layout.welcome_instruction3,
                R.layout.welcome_instruction4,
                R.layout.welcome_instruction5,
                R.layout.welcome_instruction6,
                R.layout.welcome_instruction7};

        addBottomDots(0);

        changeStatusBarColor();

        new Thread(() -> {

            int current = getItem(+1);
            if(current < layouts.length){
                viewPager.setCurrentItem(current);
            } else {
                sessionHandler.setIsFirstTimeLaunch(false);
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerChangeListener);

        DownloadBackgroundAsset loAssetDownload = new DownloadBackgroundAsset();
        loAssetDownload.execute(instances.length);
        loAssetDownload.setLoListener(message -> {
            Log.e(TAG, message);
            loAssetDownload.cancel(true);
        });
    }

    private void addBottomDots(int currentPage) {
        TextView[] dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    ViewPager.OnPageChangeListener viewPagerChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onPageSelected(int i) {
            addBottomDots(i);
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void onBackPressed() {
        if(sessionHandler.isFirstTimeLaunch()){
            Toast.makeText(getApplicationContext(), "Cannot Skip or back on this activity", Toast.LENGTH_SHORT).show();
        }
    }

    private class MyViewPagerAdapter extends PagerAdapter{

        public MyViewPagerAdapter(){

        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position],container, false);
            container.addView(view);

            return view;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    @SuppressLint("StaticFieldLeak")
    class DownloadBackgroundAsset extends AsyncTask<Integer, Integer, String> {

        OnBackgroundDownloadFinishedListener loListener;

        void setLoListener(OnBackgroundDownloadFinishedListener listener){
            this.loListener = listener;
        }

        @Override
        protected String doInBackground(Integer... integers) {
            for(int x =0; x < integers[0]; x ++){
                instances[x].SendImportRequest(WelcomeLauncherActivity.this, new OnImportAssetListener() {
                    @Override
                    public void onImportSuccess() {

                    }

                    @Override
                    public void onImportFailed(String Message) {

                    }
                });

                Log.e(TAG, "Background counting. counter : " + x);
                publishProgress((int) ((x / (float) integers[0]) * 100));
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            loListener.OnFinished("Assets has been successfully downloaded.");
            new SessionManager(WelcomeLauncherActivity.this).setIsFirstLaunched(false);
            startActivity(new Intent(WelcomeLauncherActivity.this, Activity_Dashboard.class));
            finish();
            return "Finished!";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
            super.onProgressUpdate(values);
        }
    }

    interface OnBackgroundDownloadFinishedListener{
        void OnFinished(String message);
    }
}
