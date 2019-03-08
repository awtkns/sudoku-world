package com.sigma.sudokuworld.select.down;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.sigma.sudokuworld.persistence.sharedpreferences.KeyConstants;
import com.sigma.sudokuworld.select.PairListFragment;

public class PairDetailActivity extends AbstractDrillDownActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();

            int pairID = getIntent().getIntExtra(KeyConstants.PAIR_ID_KEY, 0);
            arguments.putInt(KeyConstants.PAIR_ID_KEY, pairID);

            //Creating the detail view fragment
            PairDetailFragment fragment = new PairDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(mFragmentContainerID, fragment)
                    .commit();
        }
    }
}
