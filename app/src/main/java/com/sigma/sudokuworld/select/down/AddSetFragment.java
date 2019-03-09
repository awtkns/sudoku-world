package com.sigma.sudokuworld.select.down;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sigma.sudokuworld.R;
import com.sigma.sudokuworld.persistence.WordPairRepository;
import com.sigma.sudokuworld.select.adapters.CheckedPairRecyclerViewAdapter;

import java.util.List;


public class AddSetFragment extends AbstractDrillDownFragment {
    private OnFragmentInteractionListener mListener;
    private TextInputEditText mNameInput;
    private TextInputEditText mDescriptionInput;
    private WordPairRepository mWordPairRepository;
    private List<WordPairRepository.WordPairInformative> mWordPairInformatives;
    private CheckedPairRecyclerViewAdapter mCheckedPairRecyclerViewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWordPairRepository = new WordPairRepository(getActivity().getApplication());
        mWordPairInformatives = mWordPairRepository.getAllWordPairsInformative();
        mCheckedPairRecyclerViewAdapter = new CheckedPairRecyclerViewAdapter(mWordPairInformatives, mListener);
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
    public void onResume() {
        super.onResume();
        mWordPairInformatives = mWordPairRepository.getAllWordPairsInformative();
        mCheckedPairRecyclerViewAdapter.notifyDataSetChanged();
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
        void onCheckChangedFragmentInteraction(WordPairRepository.WordPairInformative wordPair, Boolean isChecked);
    }
}
