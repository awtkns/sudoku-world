package com.sigma.sudokuworld.select.down;

import android.os.Bundle;
import com.sigma.sudokuworld.R;
import com.sigma.sudokuworld.persistence.WordPairRepository;
import com.sigma.sudokuworld.persistence.WordSetRepository;
import com.sigma.sudokuworld.persistence.db.entities.Set;

public class AddSetActivity extends AbstractDrillDownActivity implements CheckedPairListFragment.OnFragmentInteractionListener {
    private WordSetRepository mWordSetRepository;
    private AddSetFragment mAddSetFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAddSetFragment = new AddSetFragment();
        getSupportFragmentManager().beginTransaction()
                .add(mFragmentContainerID, mAddSetFragment)
                .commit();

        mFAB.setImageResource(R.drawable.ic_save_black_24dp);
//        mFAB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String nWord = mAddPairFragment.getNativeWord();
//                String fWord = mAddPairFragment.getForeignWord();
//
//                if (nWord != null && fWord != null) {
//                    if (!nWord.isEmpty() && !fWord.isEmpty()) {
//                        mWordPairRepository.saveWordPair(nWord, fWord);
//                        finish();
//                    }
//                }
//            }
//        });
    }

    @Override
    public void onClickPairFragmentInteraction(WordPairRepository.WordPairInformative wordPair) {

    }

    @Override
    public void onLongPairClickFragmentInteraction(Set set) {

    }
}
