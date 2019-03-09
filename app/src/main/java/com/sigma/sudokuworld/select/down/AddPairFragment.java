package com.sigma.sudokuworld.select.down;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sigma.sudokuworld.R;
import com.sigma.sudokuworld.persistence.WordPairRepository;

public class AddPairFragment extends AbstractDrillDownFragment {
    private WordPairRepository.WordPairInformative mWordPair;

    private TextInputEditText mNativeWordInput;
    private TextInputEditText mForeignWordInput;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_pair, container, false);

        mNativeWordInput = view.findViewById(R.id.nativeInput);
        mForeignWordInput = view.findViewById(R.id.foreignInput);

        String str = mNativeWordInput.getText().toString();

        return view;
    }
}

