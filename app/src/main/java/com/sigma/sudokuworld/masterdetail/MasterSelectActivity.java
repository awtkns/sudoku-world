package com.sigma.sudokuworld.masterdetail;

import android.arch.lifecycle.ViewModelProviders;
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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sigma.sudokuworld.R;
import com.sigma.sudokuworld.persistence.db.entities.Set;
import com.sigma.sudokuworld.persistence.db.entities.Pair;
import com.sigma.sudokuworld.persistence.db.views.WordPair;
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

    //Set fragment listeners
    @Override
    public void onClickSetFragmentInteraction(Set set) {
        mMasterDetailViewModel.uploadSet(set);


//        Intent intent = new Intent(this, SetDetailActivity.class);
//        intent.putExtra(KeyConstants.SET_ID_KEY, set.getSetID());
//        startActivity(intent);
    }

    @Override
    public void onLongClickSetFragmentInteraction(View view, Set set) {
        Snackbar.make(view, set.getName(), Snackbar.LENGTH_LONG)
                .setAction("Delete", new DeleteSnackBarListener(set));
    }

    //Pair fragment listeners
    @Override
    public void onClickPairFragmentInteraction(WordPair wordPair) {
        Intent intent = new Intent(this, PairDetailActivity.class);
        intent.putExtra(KeyConstants.PAIR_ID_KEY, wordPair.getPairID());
        startActivity(intent);
    }

    @Override
    public void onLongPairClickFragmentInteraction(Set set) {
        //Stub
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

    public class DeleteSnackBarListener implements View.OnClickListener {
        private Set set;
        private Pair pair;

        public DeleteSnackBarListener(Set set) {
            super();
            this.set = set;
        }

        public DeleteSnackBarListener(Pair wordPair) {
            super();
            this.pair = wordPair;
        }

        @Override
        public void onClick(View v) {
            if (set != null) mMasterDetailViewModel.deleteSet(set);
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

