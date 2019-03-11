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
import com.sigma.sudokuworld.adapters.SetRecyclerViewAdapter;
import com.sigma.sudokuworld.persistence.db.entities.Set;
import com.sigma.sudokuworld.viewmodels.MasterSelectViewModel;

import java.util.List;


public class SetListFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private MasterSelectViewModel mMasterSelectViewModel;
    private List<Set> mSetList;
    private SetRecyclerViewAdapter mAdapter;

    public static SetListFragment newInstance() {
        return new SetListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMasterSelectViewModel = ViewModelProviders.of(this).get(MasterSelectViewModel.class);
        mAdapter = new SetRecyclerViewAdapter(mListener);

        LiveData<List<Set>> allSets = mMasterSelectViewModel.getAllSets();
        allSets.observe(this, new Observer<List<Set>>() {
            @Override
            public void onChanged(@Nullable List<Set> sets) {
                mAdapter.setItems(sets);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(mAdapter);
        }

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
    }
}
