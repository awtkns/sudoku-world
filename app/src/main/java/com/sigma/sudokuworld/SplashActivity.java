package com.sigma.sudokuworld;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView imageView = findViewById(R.id.sigmaAnimation);
        final AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) imageView.getDrawable();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animatedVectorDrawable.start();
            }
        });


//        Intent i = new Intent(this, MenuActivity.class);
//        startActivity(i);
    }
}
