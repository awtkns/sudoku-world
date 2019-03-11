package com.sigma.sudokuworld.masterdetail.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.sigma.sudokuworld.R;
import com.sigma.sudokuworld.viewmodels.MasterDetailViewModel;

public abstract class AbstractDrillDownActivity extends AppCompatActivity {

    CoordinatorLayout mCoordinatorLayout;
    FloatingActionButton mFAB;
    Toolbar mToolbar;
    MasterDetailViewModel mMasterDetailViewModel;
    int mFragmentContainerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drill_down);

        mMasterDetailViewModel = ViewModelProviders.of(this).get(MasterDetailViewModel.class);

        mFragmentContainerID = R.id.detail_container;

        mCoordinatorLayout = findViewById(R.id.clayout);
        mToolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(mToolbar);

        mFAB = findViewById(R.id.fab); //TODO add back button
    }
}
