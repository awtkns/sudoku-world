package com.sigma.sudokuworld.masterdetail.detail;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sigma.sudokuworld.R;
import com.sigma.sudokuworld.persistence.db.views.WordPair;
import com.sigma.sudokuworld.adapters.CheckedPairRecyclerViewAdapter;
import com.sigma.sudokuworld.viewmodels.MasterSelectViewModel;

import java.util.List;

public class AddSetFragment extends AbstractDrillDownFragment {
    private OnFragmentInteractionListener mListener;
    private MasterSelectViewModel mMasterSelectViewModel;

    private TextInputEditText mNameInput;
    private TextInputEditText mDescriptionInput;
    private CheckedPairRecyclerViewAdapter mCheckedPairRecyclerViewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMasterSelectViewModel = ViewModelProviders.of(this).get(MasterSelectViewModel.class);
        mCheckedPairRecyclerViewAdapter = new CheckedPairRecyclerViewAdapter(mListener);
        mMasterSelectViewModel.getAllWordPairs().observe(this, new Observer<List<WordPair>>() {
            @Override
            public void onChanged(@Nullable List<WordPair> wordPairs) {
                mCheckedPairRecyclerViewAdapter.setItems(wordPairs);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_set, container, false);
        mNameInput = view.findViewById(R.id.nameInput);
        mDescriptionInput = view.findViewById(R.id.descriptionInput);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(mCheckedPairRecyclerViewAdapter);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public String getSetName() {
        return mNameInput.getText().toString();
    }

    public String getSetDescription() {
        return mDescriptionInput.getText().toString();
    }

    public interface OnFragmentInteractionListener {
        void onCheckChangedFragmentInteraction(WordPair wordPair, Boolean isChecked);
    }
}
