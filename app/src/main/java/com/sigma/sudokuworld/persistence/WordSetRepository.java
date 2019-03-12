package com.sigma.sudokuworld.persistence;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import android.support.annotation.Nullable;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.*;
import com.sigma.sudokuworld.persistence.db.AppDatabase;
import com.sigma.sudokuworld.persistence.db.daos.SetDao;
import com.sigma.sudokuworld.persistence.db.daos.PairWithSetDao;
import com.sigma.sudokuworld.persistence.db.entities.Set;
import com.sigma.sudokuworld.persistence.db.entities.PairWithSet;
import com.sigma.sudokuworld.persistence.db.entities.Word;
import com.sigma.sudokuworld.persistence.db.views.WordPair;
import com.sigma.sudokuworld.persistence.firebase.FireBaseSet;
import com.sigma.sudokuworld.persistence.firebase.FireBaseWordPair;
import com.sigma.sudokuworld.persistence.firebase.FireBaseWordSet;

import java.util.LinkedList;
import java.util.List;

public class WordSetRepository {
    private PairWithSetDao mPairWithSetDao;
    private FirebaseDatabase mFireBase;
    private SetDao setDao;
    private LiveData<List<Set>> mAllSets;
    private MutableLiveData<List<FireBaseSet>> mOnlineSets;
    private LanguageRepository mLanguageRepository;
    private WordPairRepository mWordPairRepository;

    public WordSetRepository(@NonNull Application application) {
        final AppDatabase database = AppDatabase.Companion.getInstance(application);
        FirebaseApp.initializeApp(application);
        mLanguageRepository = new LanguageRepository(application);
        mWordPairRepository = new WordPairRepository(application);

        mFireBase = FirebaseDatabase.getInstance();

        mPairWithSetDao = database.getPairWithSetDao();
        setDao = database.getSetDao();
        mAllSets = setDao.getAllLiveData();


        mOnlineSets = new MutableLiveData<>();
        mFireBase.getReference().child("sets").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                FireBaseSet newSet = dataSnapshot.getValue(FireBaseSet.class);
                String key = dataSnapshot.getKey();

                if (newSet != null && key != null) {
                    newSet.setKey(dataSnapshot.getKey());

                    List<FireBaseSet> sets = mOnlineSets.getValue();
                    if (sets == null) sets = new LinkedList<>();
                    sets.add(newSet);

                    mOnlineSets.setValue(sets);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                FireBaseSet removedSet = dataSnapshot.getValue(FireBaseSet.class);

                List<FireBaseSet> sets = mOnlineSets.getValue();
                if (sets != null) {
                    if (sets.contains(removedSet)) {
                        sets.remove(removedSet);
                        mOnlineSets.setValue(sets);
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void downloadSet(FireBaseSet fireBaseSet) {
        mFireBase.getReference().child("sets").child(fireBaseSet.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FireBaseWordSet fireBaseWordSet = dataSnapshot.getValue(FireBaseWordSet.class);

                if (fireBaseWordSet != null) {

                    Set set = new Set(0, true, fireBaseWordSet.getName(), fireBaseWordSet.getDescription());
                    long setID = setDao.insert(set);

                    long nLangId = mLanguageRepository.insertLanguage("Test lang", fireBaseWordSet.getNativeLanguageCode()); //TODO: FIX LANG
                    long fLangId = mLanguageRepository.insertLanguage("Test lang", fireBaseWordSet.getForeignLanguageCode()); //TODO: FIX LANG

                    for (FireBaseWordPair wp : fireBaseWordSet.getWordPairs()) {
                        Word nWord = new Word(0, nLangId, wp.getNativeWord());
                        Word fWord = new Word(0, fLangId, wp.getForeignWord());

                        long pairID = mWordPairRepository.saveWordPair(nWord, fWord);
                        mPairWithSetDao.insert(new PairWithSet(setID, pairID));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public LiveData<List<FireBaseSet>> getOnlineSets() {
        return mOnlineSets;
    }

    public LiveData<List<Set>> getAllSets() {
        return mAllSets;
    }

    public void uploadSetToFireBase(Set set) {

        List<FireBaseWordPair> fireBaseWordPairs = new LinkedList<>();
        for (WordPair wp: getAllWordPairsInSet(set.getSetID())) {
            fireBaseWordPairs.add(new FireBaseWordPair(wp.getNativeWord().getWord(), wp.getForeignWord().getWord()));
        }

        FireBaseWordSet wordSet = new FireBaseWordSet(
                "parent",
                set.getName(),
                set.getDescription(),
                "en",
                "fr",   //TODO: fix lang
                fireBaseWordPairs);

        mFireBase.getReference().child("sets").push().setValue(wordSet);
    }

    public void deleteFireBaseSet(FireBaseSet fireBaseSet) {
        mFireBase.getReference().child("sets").child(fireBaseSet.getKey()).removeValue();
    }

    public Set getSet(long setId) {
        return setDao.getSetByID(setId);
    }

    public void deleteSet(Set set) {
        setDao.delete(set);
    }

    public void saveSet(String name, String description, List<WordPair> wordPairs) {
        Set set = new Set(0, false, name, description);
        long setId = setDao.insert(set);

        for (WordPair wp : wordPairs) {
            mPairWithSetDao.insert(new PairWithSet(setId, wp.getPairID()));
        }
    }

    public List<WordPair> getAllWordPairsInSet(long setID) {
        return mPairWithSetDao.getAllWordPairsInSet(setID);
    }

}