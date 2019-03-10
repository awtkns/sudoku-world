package com.sigma.sudokuworld.select.down;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import com.sigma.sudokuworld.R;
import com.sigma.sudokuworld.persistence.db.views.WordPair;

import java.util.LinkedList;
import java.util.List;

public class AddSetActivity extends AbstractDrillDownActivity implements AddSetFragment.OnFragmentInteractionListener {
    private AddSetFragment mAddSetFragment;
    private List<WordPair> mCheckedPairs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCheckedPairs = new LinkedList<>();

        mAddSetFragment = new AddSetFragment();
        getSupportFragmentManager().beginTransaction()
                .add(mFragmentContainerID, mAddSetFragment)
                .commit();

        mFAB.setImageResource(R.drawable.ic_save_black_24dp);
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveWordSet();
            }
        });
    }

    @Override
    public void onCheckChangedFragmentInteraction(WordPair wordPair, Boolean isChecked) {
        boolean isInList = mCheckedPairs.contains(wordPair);

        if (isChecked && !isInList) mCheckedPairs.add(wordPair);
        else if (!isChecked && isInList) mCheckedPairs.remove(wordPair);
    }

    private void saveWordSet() {
        String name = mAddSetFragment.getSetName();
        String description = mAddSetFragment.getSetDescription();;

        boolean isValidSet = true;
        String errorMsg = "";

        if (name.isEmpty()) {
            isValidSet = false;
            errorMsg = "The set needs a name.";
        }

        else if (description.isEmpty()) {
            isValidSet = false;
            errorMsg = "The set needs a description.";
        }

        else if (mCheckedPairs.isEmpty()) {
            isValidSet = false;
            errorMsg = "The set needs a needs nine words.";
        }

        if (isValidSet) {
            mMasterSelectViewModel.saveSet(name, description, mCheckedPairs);
            finish();
        } else {
            Snackbar.make(mCoordinatorLayout, errorMsg, Snackbar.LENGTH_SHORT).show();
        }
    }
}
