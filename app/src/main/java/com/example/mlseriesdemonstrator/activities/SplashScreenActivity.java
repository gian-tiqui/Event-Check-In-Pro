package com.example.mlseriesdemonstrator.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mlseriesdemonstrator.R;

import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

    ProgressBar horizontalProgressBar;
    TextView loadingText;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        startLoad();
        Handler h = new Handler();
        h.postDelayed(() -> {
            startActivity(new Intent(SplashScreenActivity.this, SelectionScreenActivity.class));
            finish();
        }, 8000);
    }

    public void startLoad() {

        loadingText = findViewById(R.id.LOADING_TEXT);
        horizontalProgressBar = findViewById(R.id.PROGRESS_BAR_HORIZONTAL);
        Timer timer = new Timer();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                counter++;
                String counterStr = counter + "%";
                loadingText.setText(counterStr);
                horizontalProgressBar.setProgress(counter);

                if(counter == 100){
                    timer.cancel();
                }
            }
        };

        timer.schedule(timerTask, 0, 100);
    }

}