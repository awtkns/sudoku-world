package com.sigma.sudokuworld.select;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sigma.sudokuworld.R;
import com.sigma.sudokuworld.persistence.WordPairRepository;
import com.sigma.sudokuworld.persistence.db.entities.Set;
import com.sigma.sudokuworld.persistence.db.views.WordPair;
import com.sigma.sudokuworld.select.adapters.PairRecyclerViewAdapter;

import java.util.List;

public class PairListFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private List<WordPair> mWordPairs;
    private PairRecyclerViewAdapter mPairRecyclerViewAdapter;
    WordPairRepository mWordPairRepository;

    public static PairListFragment newInstance() {
        return new PairListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        mWordPairRepository= new WordPairRepository(getActivity().getApplication());
        mWordPairs = mWordPairRepository.getAllWordPairs();
        mPairRecyclerViewAdapter = new PairRecyclerViewAdapter(mWordPairs, mListener);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(mPairRecyclerViewAdapter);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mWordPairs = mWordPairRepository.getAllWordPairs();
        mPairRecyclerViewAdapter.notifyDataSetChanged();
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

    public interface OnFragmentInteractionListener {
        void onClickPairFragmentInteraction(WordPair wordPair);
        void onLongPairClickFragmentInteraction(Set set);
    }
}

