package com.example.shelter;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MusicService extends Service {
    MediaPlayer player;

    @Override
    public void onCreate() {
        super.onCreate();
        if (player == null) {
            player = MediaPlayer.create(getApplicationContext(), R.raw.music_file_service);
            player.setLooping(true);
            player.setVolume(100, 100);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        player.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}