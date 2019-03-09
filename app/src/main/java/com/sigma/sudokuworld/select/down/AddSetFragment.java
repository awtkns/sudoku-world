package com.sigma.sudokuworld.select.down;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sigma.sudokuworld.R;
import com.sigma.sudokuworld.persistence.WordPairRepository;
import com.sigma.sudokuworld.persistence.db.entities.WordPair;

import java.util.ArrayList;
import java.util.List;


public class AddSetFragment extends AbstractDrillDownFragment {
    private CheckedPairListFragment mCheckedPairListFragment;
    private List<WordPair> checkedWordPairs;
    private TextInputEditText mNameInput;
    private TextInputEditText mDescriptionInput;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCheckedPairListFragment = CheckedPairListFragment.newInstance();
        checkedWordPairs = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_set, container, false);
        mNameInput = view.findViewById(R.id.nameInput);
        mDescriptionInput = view.findViewById(R.id.descriptionInput);

        getChildFragmentManager().beginTransaction()
                .replace(R.id.listContainer, mCheckedPairListFragment)
                .commit();

        return view;
    }

    public String getSetName() {
        return mNameInput.getText().toString();
    }

    public String getSetDescription() {
        return mDescriptionInput.getText().toString();
    }
}
