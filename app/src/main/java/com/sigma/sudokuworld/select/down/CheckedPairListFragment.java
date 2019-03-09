package com.sigma.sudokuworld.select.down;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sigma.sudokuworld.R;
import com.sigma.sudokuworld.persistence.WordPairRepository;
import com.sigma.sudokuworld.persistence.db.entities.Set;
import com.sigma.sudokuworld.select.PairListFragment;
import com.sigma.sudokuworld.select.adapters.CheckedPairRecyclerViewAdapter;

import java.util.List;

public class CheckedPairListFragment extends Fragment {
    private PairListFragment.OnFragmentInteractionListener mListener;
    private List<WordPairRepository.WordPairInformative> mWordPairs;
    private CheckedPairRecyclerViewAdapter mCheckedPairRecyclerViewAdapter;
    WordPairRepository mWordPairRepository;

    public static CheckedPairListFragment newInstance() {
        return new CheckedPairListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        mWordPairRepository= new WordPairRepository(getActivity().getApplication());
        mWordPairs = mWordPairRepository.getAllWordPairsInformative();
        mCheckedPairRecyclerViewAdapter = new CheckedPairRecyclerViewAdapter(mWordPairs, mListener);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(mCheckedPairRecyclerViewAdapter);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mWordPairs = mWordPairRepository.getAllWordPairsInformative();
        mCheckedPairRecyclerViewAdapter.notifyDataSetChanged();
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof PairListFragment.OnFragmentInteractionListener) {
//            mListener = (PairListFragment.OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onClickPairFragmentInteraction(WordPairRepository.WordPairInformative wordPair);
        void onLongPairClickFragmentInteraction(Set set);
    }
}

