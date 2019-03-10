package com.sigma.sudokuworld;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sigma.sudokuworld.adapters.GamePagerAdapter;
import com.sigma.sudokuworld.persistence.db.entities.Game;
import com.sigma.sudokuworld.viewmodels.MenuViewModel;

import java.util.List;

public class SelectGameFragment extends Fragment implements View.OnClickListener {

    private static final int PAGER_PADDING = 128;

    private MenuViewModel mMenuViewModel;
    private LiveData<List<Game>> mGameSaves;
    private ViewPager mViewPager;
    private Button mPlayButton;
    private Button mCancelButton;
    private Button mDeleteButton;
    private GamePagerAdapter mGamePagerAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMenuViewModel = ViewModelProviders.of(this).get(MenuViewModel.class);
        mGamePagerAdapter = new GamePagerAdapter(getFragmentManager());

        mGameSaves = mMenuViewModel.getAllGameSaves();
        mGameSaves.observe(this, new Observer<List<Game>>() {
            @Override
            public void onChanged(@Nullable List<Game> games) {
                mGamePagerAdapter.setItems(games);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_game, container, false);
        mPlayButton = view.findViewById(R.id.playButtonSelectGameFragment);
        mPlayButton.setOnClickListener(this);



        mDeleteButton = view.findViewById(R.id.deleteButtonSelectGameFragment);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenuViewModel.deleteGame(mGameSaves.getValue().get(mViewPager.getCurrentItem()));
            }
        });

        mCancelButton = view.findViewById(R.id.selectGameCancelButton);
        mCancelButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MenuActivity) getActivity()).closeFragment();
            }
        });


        mViewPager = view.findViewById(R.id.gameSavePager);
        mViewPager.setAdapter(mGamePagerAdapter);
        mViewPager.setClipToPadding(false);
        mViewPager.setPageMargin(PAGER_PADDING); //TODO convert to dp

        // Inflate the layout for this fragment
        return view;
    }

    public void onClick(View v) {
        if (v.getId() == mPlayButton.getId()) {
            if (!mGameSaves.getValue().isEmpty()) {
                try {
                    ((MenuActivity) getActivity()).startGame(mGameSaves.getValue().get(mViewPager.getCurrentItem()).getSaveID());
                } catch (NullPointerException e) {
                    Log.d("Frag", "onPlayPressed: bad stuff happened");
                }
            }
        }
    }
}
