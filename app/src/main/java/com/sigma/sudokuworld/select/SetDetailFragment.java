package com.sigma.sudokuworld.select;

import android.app.Activity;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sigma.sudokuworld.R;
import com.sigma.sudokuworld.persistence.WordSetRepository;
import com.sigma.sudokuworld.persistence.db.entities.Set;
import com.sigma.sudokuworld.persistence.sharedpreferences.KeyConstants;


public class SetDetailFragment extends Fragment {
    private Set mSet;
    private CollapsingToolbarLayout mAppBarLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WordSetRepository wordSetRepository = new WordSetRepository(getActivity().getApplication());

        int setID = getArguments().getInt(KeyConstants.SET_ID_KEY);
        mSet = wordSetRepository.getSet(setID);

        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            if (mSet != null) appBarLayout.setTitle(mSet.getName());

            AnimatedVectorDrawable avd = (AnimatedVectorDrawable) ContextCompat.getDrawable(getContext(), R.drawable.avd_menu);
            avd.start();
            appBarLayout.setBackground(avd);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_detail, container, false);
        TextView nameTextView = view.findViewById(R.id.setName);
        TextView descriptionTextView = view.findViewById(R.id.setDescription);

        if (mSet != null) {
            nameTextView.setText(mSet.getName());
            descriptionTextView.setText(mSet.getDescription());
        }

        return view;
    }
}