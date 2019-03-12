package com.sigma.sudokuworld.viewmodels;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import com.sigma.sudokuworld.persistence.WordPairRepository;
import com.sigma.sudokuworld.persistence.WordSetRepository;
import com.sigma.sudokuworld.persistence.db.entities.Set;
import com.sigma.sudokuworld.persistence.db.entities.Word;
import com.sigma.sudokuworld.persistence.db.views.WordPair;
import com.sigma.sudokuworld.persistence.firebase.FireBaseSet;
import com.sigma.sudokuworld.persistence.sharedpreferences.PersistenceService;

import java.util.List;

public class MasterDetailViewModel extends BaseSettingsViewModel {

    private WordSetRepository mWordSetRepository;
    private WordPairRepository mWordPairRepository;
    private LiveData<List<Set>> mAllSets;
    private LiveData<List<FireBaseSet>> mOnlineSets;
    private LiveData<List<WordPair>> mAllWordPairs;


    public MasterDetailViewModel(@NonNull Application application) {
        super(application);
        mWordSetRepository = new WordSetRepository(mApplication);
        mWordPairRepository = new WordPairRepository(mApplication);

        mAllSets = mWordSetRepository.getAllSets();
        mAllWordPairs = mWordPairRepository.getAllWordPairs();
        mOnlineSets = mWordSetRepository.getOnlineSets();
    }

    public LiveData<List<FireBaseSet>> getOnlineSets() {
        return mOnlineSets;
    }

    public LiveData<List<Set>> getAllSets() {
        return mAllSets;
    }

    public LiveData<List<WordPair>> getAllWordPairs() {
        return mAllWordPairs;
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

    public WordPair getWordPair(long pairID) {
        return mWordPairRepository.getWordPair(pairID);
    }

    public void saveWordPair(Word nativeWord, Word foreignWord) {
        mWordPairRepository.saveWordPair(nativeWord, foreignWord);
    }


    //FIRE BASE
    public void downLoadSet(FireBaseSet fireBaseSet) {
        mWordSetRepository.downloadSet(fireBaseSet.getKey());
    }

    public void uploadSet(Set set) {
        mWordSetRepository.uploadSetToFireBase(set);
    }

    public void deleteSet(FireBaseSet fireBaseSet){
        mWordSetRepository.deleteFireBaseSet(fireBaseSet);
    }
}
