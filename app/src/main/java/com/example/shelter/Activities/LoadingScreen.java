package com.example.shelter.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.shelter.R;

public class LoadingScreen extends AppCompatActivity {

    private ProgressBar mProgressBar;
    private TextView mLoadingText;

    private int mProgressStatus = 0;

    private Handler mHandler = new Handler();

    private ProgressBar ProgBar;
    private int progStat = 0;
    private Handler nHandler = new Handler();
    private TextView LoadText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);


        //defines both objects
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mLoadingText = (TextView) findViewById(R.id.LoadingCompleteTextView);



        new Thread(new Runnable() {
            @Override
            public void run() {
                //while it's not 100 keep loading and adding 1
                while (mProgressStatus < 100) {
                    mProgressStatus++;  //adds 1 each while loop
                    android.os.SystemClock.sleep(20);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setProgress(mProgressStatus); //update the loading bar based on the counter mProgressStatus
                        }
                    });
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mLoadingText.setVisibility(View.VISIBLE);  //if mProgressStatus is 100 then show text
                        android.os.SystemClock.sleep(100);
                        Intent intent1 = new Intent(LoadingScreen.this,LoginActivity.class); //redirects you to the login page
                        startActivity(intent1);
                        finish();
                    }
                });
            }
        }).start();
    }
}