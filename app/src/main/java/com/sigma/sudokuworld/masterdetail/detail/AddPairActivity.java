package com.sigma.sudokuworld.masterdetail.detail;

import android.os.Bundle;
import android.view.View;
import com.sigma.sudokuworld.R;
import com.sigma.sudokuworld.persistence.db.entities.Word;

public class AddPairActivity extends AbstractDrillDownActivity {
    private AddPairFragment mAddPairFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAddPairFragment = new AddPairFragment();
        getSupportFragmentManager().beginTransaction()
                .add(mFragmentContainerID, mAddPairFragment)
                .commit();

        mFAB.setImageResource(R.drawable.ic_save_black_24dp);
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nWord = mAddPairFragment.getNativeWord();
                String fWord = mAddPairFragment.getForeignWord();

                if (nWord != null && fWord != null) {
                    if (!nWord.isEmpty() && !fWord.isEmpty()) {

                        Word nativeWord = new Word(0, 1, nWord);
                        Word foreignWord = new Word(0, 1, fWord);

                        mMasterDetailViewModel.saveWordPair(nativeWord, foreignWord);
                        finish();
                    }
                }
            }
        });
    }
}
