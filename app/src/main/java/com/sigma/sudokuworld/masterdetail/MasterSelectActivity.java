package com.sigma.sudokuworld.masterdetail;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sigma.sudokuworld.R;
import com.sigma.sudokuworld.persistence.db.entities.Set;
import com.sigma.sudokuworld.persistence.db.entities.Pair;
import com.sigma.sudokuworld.persistence.db.views.WordPair;
import com.sigma.sudokuworld.persistence.firebase.FireBaseSet;
import com.sigma.sudokuworld.persistence.sharedpreferences.KeyConstants;
import com.sigma.sudokuworld.masterdetail.detail.AddPairActivity;
import com.sigma.sudokuworld.masterdetail.detail.AddSetActivity;
import com.sigma.sudokuworld.masterdetail.detail.PairDetailActivity;
import com.sigma.sudokuworld.masterdetail.detail.SetDetailActivity;
import com.sigma.sudokuworld.viewmodels.MasterDetailViewModel;

public class MasterSelectActivity extends AppCompatActivity implements SetListFragment.OnFragmentInteractionListener, PairListFragment.OnFragmentInteractionListener {

    ViewPager mViewPager;
    TabLayout mTabLayout;
    FloatingActionButton mFloatingActionButton;
    MasterDetailViewModel mMasterDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_select);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Set Builder");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            AnimatedVectorDrawable avd = (AnimatedVectorDrawable) ContextCompat.getDrawable(this, R.drawable.avd_menu);
            avd.start();
            actionBar.setBackgroundDrawable(avd);
        }

        mMasterDetailViewModel = ViewModelProviders.of(this).get(MasterDetailViewModel.class);

        mFloatingActionButton = findViewById(R.id.fab);
        mTabLayout = findViewById(R.id.tabs);
        mViewPager = findViewById(R.id.tabPager);
        mViewPager.setAdapter(new TabPagerAdapter(getSupportFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPager);

        mFloatingActionButton.setOnClickListener(new FloatingActionButtonListener());
        mFloatingActionButton.setImageResource(R.drawable.ic_add_black_24dp);
    }


    //Fire base listeners
    @Override
    public void onFireBaseClick(FireBaseSet set) {
          //Stub
    }

    @Override
    public void onFireBaseLongClick(View view, final FireBaseSet set) {
        new AlertDialog.Builder(this)
                .setTitle(set.getName())
                .setPositiveButton("Download", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mMasterDetailViewModel.downLoadSet(set);
                    }
                })
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mMasterDetailViewModel.deleteSet(set);
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

    //Set fragment listener
    @Override
    public void onClickSetFragmentInteraction(Set set) {
        Intent intent = new Intent(this, SetDetailActivity.class);
        intent.putExtra(KeyConstants.SET_ID_KEY, set.getSetID());
        startActivity(intent);
    }

    @Override
    public void onLongClickSetFragmentInteraction(View view, final Set set) {
        new AlertDialog.Builder(this)
                .setTitle(set.getName())
                .setPositiveButton("Upload", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mMasterDetailViewModel.uploadSet(set);
                    }
                })
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mMasterDetailViewModel.deleteSet(set);
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

    //Pair fragment listeners
    @Override
    public void onClickPairFragmentInteraction(WordPair wordPair) {
        //TODO disabled for demo
//        Intent intent = new Intent(this, PairDetailActivity.class);
//        intent.putExtra(KeyConstants.PAIR_ID_KEY, wordPair.getPairID());
//        startActivity(intent);
    }

    @Override
    public void onLongPairClickFragmentInteraction(final WordPair wordPair) {
        String msg = wordPair.getNativeWord().getWord() + " " + wordPair.getForeignWord().getWord();

        Snackbar.make(findViewById(R.id.tabPager), msg, Snackbar.LENGTH_LONG)
                .setAction("Delete", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMasterDetailViewModel.deletePair(wordPair);
                    }
                }).show();
    }

    public class FloatingActionButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            Intent intent;
            if (mTabLayout.getSelectedTabPosition() == 0) {
                intent = new Intent(getBaseContext(), AddSetActivity.class);
            } else {
                intent = new Intent(getBaseContext(), AddPairActivity.class);
            }

            startActivity(intent);
        }
    }

    public class TabPagerAdapter extends FragmentPagerAdapter {
        private String[] tabTitles = new String[]{"Sets", "Pairs"};

        public TabPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: return SetListFragment.newInstance();
                case 1:
                default:
                    return PairListFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }
}

