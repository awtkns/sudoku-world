package com.sigma.sudokuworld.masterdetail.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sigma.sudokuworld.R;
import com.sigma.sudokuworld.adapters.PairRecyclerViewAdapter;
import com.sigma.sudokuworld.persistence.db.entities.Set;
import com.sigma.sudokuworld.persistence.db.views.WordPair;
import com.sigma.sudokuworld.persistence.sharedpreferences.KeyConstants;
import com.sigma.sudokuworld.viewmodels.MasterSelectViewModel;

import java.util.List;


public class SetDetailFragment extends AbstractDrillDownFragment {
    private Set mSet;
    private List<WordPair> mWordPairs;
    private MasterSelectViewModel mMasterSelectViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMasterSelectViewModel = ViewModelProviders.of(this).get(MasterSelectViewModel.class);
        mSet = mMasterSelectViewModel.getSet(getArguments().getLong(KeyConstants.SET_ID_KEY));
        mWordPairs = mMasterSelectViewModel.getWordsInSet(mSet);

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

        PairRecyclerViewAdapter adapter = new PairRecyclerViewAdapter(mWordPairs, null);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }
}