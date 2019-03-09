package com.sigma.sudokuworld;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sigma.sudokuworld.persistence.GameRepository;
import com.sigma.sudokuworld.persistence.db.entities.Game;
import com.sigma.sudokuworld.persistence.sharedpreferences.KeyConstants;

import java.util.List;

public class SelectGameFragment extends Fragment implements View.OnClickListener {

    private static final int PAGER_PADDING = 128;

    private List<Game> gameSaves;
    private ViewPager mViewPager;
    private Button mPlayButton;
    private Button mCancelButton;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GameRepository gameRepository = new GameRepository(getActivity().getApplication());
        gameSaves = gameRepository.getAllGames();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_game, container, false);
        mPlayButton = view.findViewById(R.id.playButtonSelectGameFragment);
        mPlayButton.setOnClickListener(this);

        mCancelButton = view.findViewById(R.id.selectGameCancelButton);
        mCancelButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MenuActivity) getActivity()).closeFragment();
            }
        });

        PagerAdapter pagerAdapter = new GamePagerAdapter(getFragmentManager());
        mViewPager = view.findViewById(R.id.gameSavePager);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setClipToPadding(false);
        mViewPager.setPageMargin(PAGER_PADDING); //TODO convert to dp

        // Inflate the layout for this fragment
        return view;
    }

    public void onClick(View v) {
        if (v.getId() == mPlayButton.getId()) {
            if (!gameSaves.isEmpty()) {
                try {
                    ((MenuActivity) getActivity()).startGame(gameSaves.get(mViewPager.getCurrentItem()).getSaveID());
                } catch (NullPointerException e) {
                    Log.d("Frag", "onPlayPressed: bad stuff happened");
                }
            }
        }
    }

    private class GamePagerAdapter extends FragmentStatePagerAdapter {
        public GamePagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            GamePageFragment fragment = new GamePageFragment();

            Bundle args = new Bundle();
            args.putBooleanArray(KeyConstants.LOCKED_CELLS_KEY, gameSaves.get(position).getLockedCells());
            args.putString(KeyConstants.DIFFICULTY_KEY, gameSaves.get(position).getDifficulty().toString());
            args.putString(KeyConstants.MODE_KEY, gameSaves.get(position).getGameMode().toString());
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public int getCount() {
            return gameSaves.size();
        }
    }
}
