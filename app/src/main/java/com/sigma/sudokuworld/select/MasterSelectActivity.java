package com.sigma.sudokuworld.select;

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
import com.sigma.sudokuworld.persistence.WordPairRepository;
import com.sigma.sudokuworld.persistence.WordSetRepository;
import com.sigma.sudokuworld.persistence.db.entities.Set;
import com.sigma.sudokuworld.persistence.db.entities.WordPair;
import com.sigma.sudokuworld.persistence.sharedpreferences.KeyConstants;
import com.sigma.sudokuworld.select.down.AddPairActivity;
import com.sigma.sudokuworld.select.down.AddSetActivity;
import com.sigma.sudokuworld.select.down.PairDetailActivity;
import com.sigma.sudokuworld.select.down.SetDetailActivity;

public class MasterSelectActivity extends AppCompatActivity implements SetListFragment.OnFragmentInteractionListener, PairListFragment.OnFragmentInteractionListener {

    WordSetRepository mWordSetRepository;
    ViewPager mViewPager;
    TabLayout mTabLayout;
    FloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_select);

        mWordSetRepository = new WordSetRepository(getApplication());

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Set Builder");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            AnimatedVectorDrawable avd = (AnimatedVectorDrawable) ContextCompat.getDrawable(this, R.drawable.avd_menu);
            avd.start();
            actionBar.setBackgroundDrawable(avd);
        }

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
        Intent intent = new Intent(this, SetDetailActivity.class);
        intent.putExtra(KeyConstants.SET_ID_KEY, set.getSetID());
        startActivity(intent);
    }

    @Override
    public void onLongClickSetFragmentInteraction(View view, Set set) {
        String msg = "Delete the '" + set.getName() + "' word set?";
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
                .setAction("Delete", new DeleteSnackBarListener(set)).show();
    }

    //Pair fragment listeners
    @Override
    public void onClickPairFragmentInteraction(WordPairRepository.WordPairInformative wordPair) {
        Intent intent = new Intent(this, PairDetailActivity.class);
        intent.putExtra(KeyConstants.PAIR_ID_KEY, wordPair.getWordPair().getWordPairID());
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
        private WordPair pair;

        public DeleteSnackBarListener(Set set) {
            super();
            this.set = set;
        }

        public DeleteSnackBarListener(WordPair wordPair) {
            super();
            this.pair = wordPair;
        }

        @Override
        public void onClick(View v) {
            if (set != null) mWordSetRepository.deleteSet(set);
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

