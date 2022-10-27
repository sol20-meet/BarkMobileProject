package com.example.shelter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.Toast;

public class MyReceiverBattery extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int BatteryPercentage=intent.getIntExtra("level",0);
        if(BatteryPercentage == 15)
        {
            final MediaPlayer dogSoundMP = MediaPlayer.create(context, R.raw.dog_bark_sound);
            Toast.makeText(context, "Battery is low, Charge your phone!",Toast.LENGTH_LONG).show();
            dogSoundMP.start();
        }
    }
}