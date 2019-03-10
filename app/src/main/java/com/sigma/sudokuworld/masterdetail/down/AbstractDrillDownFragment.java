package com.sigma.sudokuworld.masterdetail.down;

import android.app.Activity;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.sigma.sudokuworld.R;

public abstract class AbstractDrillDownFragment extends Fragment {
    protected CollapsingToolbarLayout mAppBarLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Activity activity = this.getActivity();
        mAppBarLayout = activity.findViewById(R.id.toolbar_layout);
        mAppBarLayout.setExpandedTitleColor(getResources().getColor(R.color.colorBlack));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            AnimatedVectorDrawable avd = (AnimatedVectorDrawable) ContextCompat.getDrawable(getContext(), R.drawable.avd_menu);
            avd.start();
            mAppBarLayout.setBackground(avd);
        }
    }
}
