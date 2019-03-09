package com.sigma.sudokuworld.select.down;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sigma.sudokuworld.R;
import com.sigma.sudokuworld.persistence.WordPairRepository;
import com.sigma.sudokuworld.persistence.sharedpreferences.KeyConstants;

public class PairDetailFragment extends AbstractDrillDownFragment {
    private WordPairRepository.WordPairInformative mWordPair;
    private TextView mNativeWord;
    private TextView mNativeLanguage;
    private TextView mForeignWord;
    private TextView mForeignLanguage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WordPairRepository wordPairRepository = new WordPairRepository(getActivity().getApplication());
        mWordPair = wordPairRepository.getWordPairInformative(getArguments().getInt(KeyConstants.PAIR_ID_KEY));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pair_detail, container, false);
        mNativeWord = view.findViewById(R.id.nativeWord);
        mNativeLanguage = view.findViewById(R.id.nativeLanguage);
        mForeignWord = view.findViewById(R.id.foriegnWord);
        mForeignLanguage = view.findViewById(R.id.foriegnLanguage);


        if (mWordPair != null) {
            mAppBarLayout.setTitle(mWordPair.getNativeWordString() + " - " + mWordPair.getForeignWordString());

            mNativeWord.setText(mWordPair.getNativeWordString());
            mNativeLanguage.setText(mWordPair.getNativeWordString()); //TODO: JOIN IN REPO TO GET LANG
        }

        return view;
    }
}
