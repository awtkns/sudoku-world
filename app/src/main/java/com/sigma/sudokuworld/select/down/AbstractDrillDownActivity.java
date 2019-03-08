package com.sigma.sudokuworld.select.down;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.sigma.sudokuworld.R;

public abstract class AbstractDrillDownActivity extends AppCompatActivity {

    FloatingActionButton mFAB;
    Toolbar mToolbar;
    int mFragmentContainerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drill_down);

        mFragmentContainerID = R.id.detail_container;

        mToolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(mToolbar);

        mFAB = findViewById(R.id.fab);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar(); //TODO: back btn
    }
}
