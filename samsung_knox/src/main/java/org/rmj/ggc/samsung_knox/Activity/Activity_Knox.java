package org.rmj.ggc.samsung_knox.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import org.rmj.g3appdriver.dev.AppData;
import org.rmj.ggc.samsung_knox.R;

public class Activity_Knox extends AppCompatActivity {

    private CardView btnKnoxUpload;
    private CardView btnKnoxActivate;
    private CardView btnKnoxUnlock;
    private CardView btnKnoxGetPIN;
    private CardView btnKnoxOffPIN;
    private CardView btnKnoxCheckSt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knox);


        Toolbar toolbar = findViewById(R.id.toolbar_knox);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnKnoxUpload = findViewById(R.id.btn_knoxUpload);
        btnKnoxActivate = findViewById(R.id.btn_knoxActivate);
        btnKnoxUnlock = findViewById(R.id.btn_knoxUnlock);
        btnKnoxGetPIN = findViewById(R.id.btn_knoxGetPin);
        btnKnoxOffPIN = findViewById(R.id.btn_knoxOfflinePIN);
        btnKnoxCheckSt = findViewById(R.id.btn_knoxCheckStatus);
        setupView();

        btnKnoxUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Knox.this, Activity_KnoxAction.class);
                intent.putExtra("knox", 0);
                startActivity(intent);
            }
        });

        btnKnoxActivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Knox.this, Activity_KnoxAction.class);
                intent.putExtra("knox", 1);
                startActivity(intent);
            }
        });

        btnKnoxUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Knox.this, Activity_KnoxAction.class);
                intent.putExtra("knox", 5);
                startActivity(intent);
            }
        });

        btnKnoxOffPIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Knox.this, Activity_KnoxAction.class);
                intent.putExtra("knox", 2);
                startActivity(intent);
            }
        });

        btnKnoxGetPIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Knox.this, Activity_KnoxAction.class);
                intent.putExtra("knox", 3);
                startActivity(intent);
            }
        });

        btnKnoxCheckSt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Knox.this, Activity_KnoxAction.class);
                intent.putExtra("knox", 4);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupView() {
        String DeptCode = AppData.getInstance(Activity_Knox.this).getDepartmentID();
        if (DeptCode.equalsIgnoreCase(org.rmj.g3appdriver.dev.DeptCode.Mobile_Phone) ||
                DeptCode.equalsIgnoreCase(org.rmj.g3appdriver.dev.DeptCode.Mobile_Phone_1)) {
            btnKnoxActivate.setVisibility(View.VISIBLE);
            btnKnoxCheckSt.setVisibility(View.VISIBLE);
        } else if (DeptCode.equalsIgnoreCase(org.rmj.g3appdriver.dev.DeptCode.Management_Information_System)) {
            btnKnoxUpload.setVisibility(View.VISIBLE);
            btnKnoxActivate.setVisibility(View.VISIBLE);
            btnKnoxUnlock.setVisibility(View.VISIBLE);
            btnKnoxGetPIN.setVisibility(View.VISIBLE);
            btnKnoxOffPIN.setVisibility(View.VISIBLE);
            btnKnoxCheckSt.setVisibility(View.VISIBLE);
        }
    }
}
