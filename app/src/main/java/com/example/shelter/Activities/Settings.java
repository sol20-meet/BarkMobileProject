package com.example.shelter.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shelter.Classes.PrefConfig;
import com.example.shelter.MusicService;
import com.example.shelter.MyReceiverBattery;
import com.example.shelter.R;
import com.google.firebase.auth.FirebaseAuth;

public class Settings extends AppCompatActivity {

    Button btSignOut, btBatteryStart , btnMusic;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    MyReceiverBattery myReceiverBattery;
    ImageView imgBark;
    int count = 0;
    Intent serviceIntent;
    boolean isOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        btSignOut = findViewById(R.id.btSignOut);
        btBatteryStart = findViewById(R.id.btBatteryStart);
        mAuth = FirebaseAuth.getInstance();
        imgBark = findViewById(R.id.imgBark);
        isOn = false;
        btnMusic = findViewById(R.id.btnMusic);
        serviceIntent = new Intent(this, MusicService.class);
        myReceiverBattery = new MyReceiverBattery();
        registerReceiver(myReceiverBattery, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        btSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent1 = new Intent(Settings.this, LoginActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                finish();
            }
        });

        btBatteryStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComponentName component = new ComponentName(getApplicationContext(), MyReceiverBattery.class);
                int status = getApplicationContext().getPackageManager().getComponentEnabledSetting(component);
                    getApplicationContext().getPackageManager().setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED , PackageManager.DONT_KILL_APP);
                    Toast.makeText(Settings.this, "Battery Alert is now On", Toast.LENGTH_SHORT).show();
            }
        });


        imgBark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count == 5) {
                    Intent intent1 = new Intent(Settings.this, Egg.class);
                    startActivity(intent1);
                    count = 0;
                }
                else {count++;}
            }
        });



        btnMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOn == false){
                    isOn = true;
                    startService(serviceIntent);
                }
                else {
                    stopService(serviceIntent);
                    isOn = false;
                }
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myReceiverBattery);
        if(isOn)
            stopService(serviceIntent);

    }
}



//if(status == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
//                    //Disable
//                    getApplicationContext().getPackageManager().setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED , PackageManager.DONT_KILL_APP);
//                    Toast.makeText(Settings.this, "Battery Alert is now Off", Toast.LENGTH_SHORT).show();
//                } else if(status == PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
//Enable