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
import com.sigma.sudokuworld.select.adapters.PairRecyclerViewAdapter;

import java.util.List;


public class PairListFragment extends Fragment {
    private OnPairListFragmentInteractionListener mListener;
    private List<WordPairRepository.WordPairInformative> mWordPairs;

    public static PairListFragment newInstance() {
        return new PairListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_list, container, false);

        WordPairRepository repository = new WordPairRepository(getActivity().getApplication());
        mWordPairs = repository.getAllWordPairsInformative();

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new PairRecyclerViewAdapter(mWordPairs, mListener));
        }

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPairListFragmentInteractionListener) {
            mListener = (OnPairListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnPairListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnPairListFragmentInteractionListener {
        void onPairListFragmentInteraction(WordPairRepository.WordPairInformative wordPair);
    }
}

