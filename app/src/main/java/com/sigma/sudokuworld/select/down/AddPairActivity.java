package com.sigma.sudokuworld.select.down;

import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import com.sigma.sudokuworld.R;
import com.sigma.sudokuworld.persistence.WordPairRepository;

public class AddPairActivity extends AbstractDrillDownActivity {
    private WordPairRepository mWordPairRepository;
    private AddPairFragment mAddPairFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWordPairRepository = new WordPairRepository(getApplication());

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
                        mWordPairRepository.saveWordPair(nWord, fWord);
                        finish();
                    }
                }
            }
        });

    }
}
