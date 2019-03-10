package com.sigma.sudokuworld.masterdetail.down;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sigma.sudokuworld.R;
import com.sigma.sudokuworld.persistence.WordSetRepository;
import com.sigma.sudokuworld.persistence.db.entities.Set;
import com.sigma.sudokuworld.persistence.sharedpreferences.KeyConstants;


public class SetDetailFragment extends AbstractDrillDownFragment {
    private Set mSet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WordSetRepository wordSetRepository = new WordSetRepository(getActivity().getApplication());
        int setID = getArguments().getInt(KeyConstants.SET_ID_KEY);
        mSet = wordSetRepository.getSet(setID);
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