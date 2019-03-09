package com.sigma.sudokuworld.select.down;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import com.sigma.sudokuworld.R;

public class AddPairActivity extends AbstractDrillDownActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFAB.setImageResource(R.drawable.ic_save_black_24dp);
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (savedInstanceState == null) {
            //Creating the detail view fragment
            AddPairFragment fragment = new AddPairFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(mFragmentContainerID, fragment)
                    .commit();
        }
    }
}
