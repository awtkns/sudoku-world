package com.sigma.sudokuworld;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView imageView = findViewById(R.id.sigmaAnimation);
        final AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) imageView.getDrawable();

        //Timer starts animation on launch and exits splash screen
        TimerTask animationStartTask = new TimerTask() {
            @Override
            public void run() {
                animatedVectorDrawable.start();
            }
        };

        TimerTask exitSplashTask = new TimerTask() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, MenuActivity.class);
                startActivity(i);
            }
        };

        Timer timer = new Timer();
        timer.schedule(animationStartTask, 1500);
        timer.schedule(exitSplashTask, 5000);
    }

    /**
     * Close the activity
     */
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
