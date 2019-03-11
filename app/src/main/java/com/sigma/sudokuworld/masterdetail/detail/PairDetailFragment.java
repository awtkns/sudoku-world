package com.sigma.sudokuworld.masterdetail.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sigma.sudokuworld.R;
import com.sigma.sudokuworld.persistence.db.views.WordPair;
import com.sigma.sudokuworld.persistence.sharedpreferences.KeyConstants;
import com.sigma.sudokuworld.viewmodels.MasterSelectViewModel;

public class PairDetailFragment extends AbstractDrillDownFragment {
    private MasterSelectViewModel mMasterSelectViewModel;
    private WordPair mWordPair;
    private TextView mNativeWord;
    private TextView mNativeLanguage;
    private TextView mForeignWord;
    private TextView mForeignLanguage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMasterSelectViewModel = ViewModelProviders.of(this).get(MasterSelectViewModel.class);
        mWordPair = mMasterSelectViewModel.getWordPair(getArguments().getInt(KeyConstants.PAIR_ID_KEY));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pair_detail, container, false);
        mNativeWord = view.findViewById(R.id.nativeWord);
        mNativeLanguage = view.findViewById(R.id.nativeLanguage);
        mForeignWord = view.findViewById(R.id.foriegnWord);
        mForeignLanguage = view.findViewById(R.id.foriegnLanguage);


        if (mWordPair != null) {
            mAppBarLayout.setTitle(mWordPair.getNativeWord() + " - " + mWordPair.getForeignWord());

            mNativeWord.setText(mWordPair.getNativeWord().getWord());
            mNativeLanguage.setText(mWordPair.getForeignWord().getWord()); //TODO: JOIN IN REPO TO GET LANG
        }

        return view;
    }
}
