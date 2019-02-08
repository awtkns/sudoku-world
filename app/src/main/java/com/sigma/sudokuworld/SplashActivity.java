package com.sigma.sudokuworld;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {
    ConstraintLayout animationLayout;
    Timer timer = new Timer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //HIDE STATUS BAR
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);


        //If we click the layout we can skip the animation and cancel the timer
        animationLayout = findViewById(R.id.animationLayout);
        animationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                startActivity(new Intent(SplashActivity.this, MenuActivity.class));
            }
        });


        ImageView imageView = findViewById(R.id.sigmaAnimation);
        final AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) imageView.getDrawable();

        //Timer starts animation on launch and exits splash screen
        TimerTask animationStartTask = new TimerTask() {
            @Override
            public void run() {
                animatedVectorDrawable.start();
            }
        };

        final TimerTask exitSplashTask = new TimerTask() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, MenuActivity.class);
                startActivity(i);
            }
        };

        timer.schedule(animationStartTask, 500); //SHOULD BE 1500
        timer.schedule(exitSplashTask, 4000);     //SHOULD BE 5000

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
