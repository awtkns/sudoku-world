package com.sigma.sudokuworld.masterdetail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sigma.sudokuworld.R;
import com.sigma.sudokuworld.adapters.FireBaseSetRecycleViewAdapter;
import com.sigma.sudokuworld.adapters.SetRecyclerViewAdapter;
import com.sigma.sudokuworld.persistence.db.entities.Set;
import com.sigma.sudokuworld.persistence.firebase.FireBaseSet;
import com.sigma.sudokuworld.viewmodels.MasterDetailViewModel;

import java.util.List;


public class SetListFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private MasterDetailViewModel mMasterDetailViewModel;
    private SetRecyclerViewAdapter mAdapter;
    private FireBaseSetRecycleViewAdapter mOnlineAdapter;
    public static SetListFragment newInstance() {
        return new SetListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMasterDetailViewModel = ViewModelProviders.of(this).get(MasterDetailViewModel.class);
        mAdapter = new SetRecyclerViewAdapter(mListener);
        mOnlineAdapter = new FireBaseSetRecycleViewAdapter(mListener);

        LiveData<List<Set>> allSets = mMasterDetailViewModel.getAllSets();
        allSets.observe(this, new Observer<List<Set>>() {
            @Override
            public void onChanged(@Nullable List<Set> sets) {
                mAdapter.setItems(sets);
            }
        });

        LiveData<List<FireBaseSet>> onlineSets = mMasterDetailViewModel.getOnlineSets();
        onlineSets.observe(this, new Observer<List<FireBaseSet>>() {
            @Override
            public void onChanged(@Nullable List<FireBaseSet> sets) {
                if (sets != null) {
                    mOnlineAdapter.setItems(sets);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_master, container, false);

        RecyclerView localView = view.findViewById(R.id.localRecycler);
        RecyclerView onlineView = view.findViewById(R.id.onlineRecycler);

        localView.setLayoutManager(new LinearLayoutManager(localView.getContext()));
        localView.setAdapter(mAdapter);

        onlineView.setLayoutManager(new LinearLayoutManager(localView.getContext()));
        onlineView.setAdapter(mOnlineAdapter);


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSetListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onClickSetFragmentInteraction(Set set);
        void onLongClickSetFragmentInteraction(View view, Set set);
        void onFireBaseClick(FireBaseSet set);
        void onFireBaseLongClick(View view, FireBaseSet set);
    }
}
