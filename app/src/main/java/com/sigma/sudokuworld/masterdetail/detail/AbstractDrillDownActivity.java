package com.sigma.sudokuworld.masterdetail.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.sigma.sudokuworld.R;
import com.sigma.sudokuworld.viewmodels.MasterSelectViewModel;

public abstract class AbstractDrillDownActivity extends AppCompatActivity {

    CoordinatorLayout mCoordinatorLayout;
    FloatingActionButton mFAB;
    Toolbar mToolbar;
    MasterSelectViewModel mMasterSelectViewModel;
    int mFragmentContainerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drill_down);

        mMasterSelectViewModel = ViewModelProviders.of(this).get(MasterSelectViewModel.class);

        mFragmentContainerID = R.id.detail_container;

        mCoordinatorLayout = findViewById(R.id.clayout);
        mToolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(mToolbar);

        mFAB = findViewById(R.id.fab); //TODO add back button
    }
}
