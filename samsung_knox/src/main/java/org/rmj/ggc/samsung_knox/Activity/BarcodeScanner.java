package org.rmj.ggc.samsung_knox.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.rmj.ggc.samsung_knox.R;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BarcodeScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static final String TAG = BarcodeScanner.class.getSimpleName();

    private ZXingScannerView scanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        scanner = findViewById(R.id.scanner);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Dexter.withActivity(BarcodeScanner.this)
                    .withPermission(Manifest.permission.CAMERA)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
                            scanner.setResultHandler(BarcodeScanner.this);
                            scanner.startCamera();
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {
                            Toast.makeText(BarcodeScanner.this, "Please allow the permision", Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(BarcodeScanner.this, Activity_KnoxAction.class);
        intent.putExtra("code", barcode);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(BarcodeScanner.this, Activity_KnoxAction.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
    }

    @Override
    public void handleResult(Result rawResult) {
        checkKnoxActivity(rawResult.toString());
        finish();
    }
}
