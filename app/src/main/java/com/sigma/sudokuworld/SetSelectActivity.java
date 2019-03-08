package com.sigma.sudokuworld;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sigma.sudokuworld.persistence.db.entities.Set;
import com.sigma.sudokuworld.persistence.sharedpreferences.KeyConstants;

public class SetSelectActivity extends AppCompatActivity implements SetListFragment.OnListFragmentInteractionListener {

    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_select);

        final ActionBar actionBar = getSupportActionBar();
        AnimatedVectorDrawable avd = (AnimatedVectorDrawable) ContextCompat.getDrawable(this, R.drawable.avd_menu);
        avd.start();
        actionBar.setBackgroundDrawable(avd);

        FloatingActionButton fab = findViewById(R.id.newSetFAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        mViewPager = findViewById(R.id.tabPager);
        mViewPager.setAdapter(new TabPagerAdapter(getSupportFragmentManager()));
    }

    @Override
    public void onListFragmentInteraction(Set set) {
        Intent intent = new Intent(this, SetDetailActivity.class);
        intent.putExtra(KeyConstants.SET_ID_KEY, set.getSetID());
        startActivity(intent);
    }

    public class TabPagerAdapter extends FragmentPagerAdapter {
        public TabPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public Fragment getItem(int position) {
            switch (position) {
                case 0: return SetListFragment.newInstance();
                case 1: return SetListFragment.newInstance();
            }

            return SetListFragment.newInstance();
        }

        public int getCount() {
            return 2;
        }
    }
}

