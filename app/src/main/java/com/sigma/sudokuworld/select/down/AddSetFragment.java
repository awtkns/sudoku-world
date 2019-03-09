package com.sigma.sudokuworld.select.down;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sigma.sudokuworld.R;
import com.sigma.sudokuworld.persistence.WordPairRepository;
import com.sigma.sudokuworld.persistence.db.entities.Set;


public class AddSetFragment extends AbstractDrillDownFragment implements CheckedPairListFragment.OnFragmentInteractionListener {
    private CheckedPairListFragment mCheckedPairListFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCheckedPairListFragment = CheckedPairListFragment.newInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_pair, container, false);

        getChildFragmentManager().beginTransaction()
                .replace(R.id.listContainer, mCheckedPairListFragment)
                .commit();

        return view;
    }


    @Override
    public void onClickPairFragmentInteraction(WordPairRepository.WordPairInformative wordPair) {

    }

    @Override
    public void onLongPairClickFragmentInteraction(Set set) {

    }
}
