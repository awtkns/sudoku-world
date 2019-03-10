package com.sigma.sudokuworld.masterdetail.detail;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sigma.sudokuworld.R;

public class AddPairFragment extends AbstractDrillDownFragment {
    private TextInputEditText mNativeWordInput;
    private TextInputEditText mForeignWordInput;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_pair, container, false);

        mAppBarLayout.setTitle("Add Word Pair");
        mNativeWordInput = view.findViewById(R.id.nativeInput);
        mForeignWordInput = view.findViewById(R.id.foreignInput);

        return view;
    }

    public String getNativeWord() {
        return mNativeWordInput.getText().toString();
    }

    public String getForeignWord() {
        return mForeignWordInput.getText().toString();
    }
}

