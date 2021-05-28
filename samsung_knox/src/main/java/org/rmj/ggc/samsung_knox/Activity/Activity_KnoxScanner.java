package org.rmj.ggc.samsung_knox.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.rmj.ggc.samsung_knox.Fragments.Fragment_KnoxActivate;
import org.rmj.ggc.samsung_knox.R;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Activity_KnoxScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static final String TAG = Activity_KnoxScanner.class.getSimpleName();
    private ZXingScannerView scanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knox_scanner);

        scanner = findViewById(R.id.scanner);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Dexter.withActivity(Activity_KnoxScanner.this)
                    .withPermission(Manifest.permission.CAMERA)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
                            scanner.setResultHandler(Activity_KnoxScanner.this);
                            scanner.startCamera();
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {
                            Toast.makeText(Activity_KnoxScanner.this, "Please allow the permision", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                        }
                    })
                    .check();
        }
    }
    @Override
    protected void onDestroy() {
        scanner.stopCamera();
        super.onDestroy();
    }

    private void checkKnoxActivity(String barcode){
        new Fragment_KnoxActivate().getInstance().setImei(barcode);

        /*Intent intent = new Intent(Activity_KnoxScanner.this, Activity_KnoxAction.class);
        intent.putExtra("code", barcode);
        startActivity(intent);*/
    }

    @Override
    public void onBackPressed() {
        /*Intent intent = new Intent(Activity_KnoxScanner.this, Activity_KnoxAction.class);
        startActivity(intent);*/
        finish();
    }

    @Override
    public void handleResult(Result rawResult) {
        Log.e(TAG, rawResult.toString());
        checkKnoxActivity(rawResult.toString());
        finish();
    }
}
