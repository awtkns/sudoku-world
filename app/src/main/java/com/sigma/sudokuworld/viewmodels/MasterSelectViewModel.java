package com.sigma.sudokuworld.viewmodels;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import com.sigma.sudokuworld.persistence.WordSetRepository;
import com.sigma.sudokuworld.persistence.db.entities.Set;
import com.sigma.sudokuworld.persistence.db.views.WordPair;
import com.sigma.sudokuworld.persistence.sharedpreferences.PersistenceService;

import java.util.List;

public class MasterSelectViewModel extends BaseSettingsViewModel {

    private LiveData<List<Set>> mAllSets;
    private WordSetRepository mWordSetRepository;

    public MasterSelectViewModel(@NonNull Application application) {
        super(application);

        mWordSetRepository = new WordSetRepository(mApplication);
        mAllSets = mWordSetRepository.getAllSets();
    }

    public LiveData<List<Set>> getAllSets() {
        return mAllSets;
    }

    public void setSelectedSet(long setID) {
        PersistenceService.saveSetSetting(mApplication, setID);
    }

    public void setSelectedSet(Set set) {
        PersistenceService.saveSetSetting(mApplication, set.getSetID());
    }

    public Set getSelectedSet() {
        return mWordSetRepository.getSet(PersistenceService.loadSetSettingSetting(mApplication));
    }

    public Set getSet(long setID) {
        return mWordSetRepository.getSet(setID);
    }

    public void saveSet(String name, String description, List<WordPair> wordPairs) {
        mWordSetRepository.saveSet(name, description, wordPairs);
    }

    public void deleteSet(Set set) {
        mWordSetRepository.deleteSet(set);
    }

    public List<WordPair> getWordsInSet(Set set) {
        return mWordSetRepository.getAllWordPairsInSet(set.getSetID());
    }
}
