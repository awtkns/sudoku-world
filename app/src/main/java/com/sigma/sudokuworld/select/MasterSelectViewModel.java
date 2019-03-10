package com.sigma.sudokuworld.select;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import com.sigma.sudokuworld.persistence.WordSetRepository;
import com.sigma.sudokuworld.persistence.db.entities.Set;
import com.sigma.sudokuworld.persistence.db.views.WordPair;

import java.util.List;

public class MasterSelectViewModel extends AndroidViewModel {

    private LiveData<List<Set>> mAllSets;
    private WordSetRepository mWordSetRepository;

    public MasterSelectViewModel(@NonNull Application application) {
        super(application);

        mWordSetRepository = new WordSetRepository(application);
        mAllSets = mWordSetRepository.getAllSets();
    }

    public LiveData<List<Set>> getAllSets() {
        return mAllSets;
    }

    public void saveSet(String name, String description, List<WordPair> wordPairs) {
        mWordSetRepository.saveSet(name, description, wordPairs);
    }
}
