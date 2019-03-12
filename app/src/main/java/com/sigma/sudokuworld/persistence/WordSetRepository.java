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
import com.sigma.sudokuworld.persistence.db.views.WordSet;

import java.util.LinkedList;
import java.util.List;

public class WordSetRepository {
    private PairWithSetDao mPairWithSetDao;
    private FirebaseDatabase mFireBase;
    private SetDao setDao;
    private LiveData<List<Set>> mAllSets;
    private MutableLiveData<List<Set>> mOnlineSets;
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

                WordSet newWordSet = dataSnapshot.getValue(WordSet.class);

                if (newWordSet != null) {
                    Set newSet = newWordSet.getSet();
                    newSet.setDescription(dataSnapshot.getKey());

                    List<Set> sets = mOnlineSets.getValue();
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
                Set removedSet = dataSnapshot.getValue(Set.class);

                List<Set> sets = mOnlineSets.getValue();
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

    public void downloadSet(String key) {
        mFireBase.getReference().child("sets").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                WordSet wordSet = dataSnapshot.getValue(WordSet.class);

                Set set = new Set(0, "[Downloaded] " + wordSet.getSet().getName(), wordSet.getSet().getDescription());
                long setID = setDao.insert(set);

                long nLangId = mLanguageRepository.insertLanguage("Test lang", wordSet.getNativeLanguageCode()); //TODO: FIX LANG
                long fLangId = mLanguageRepository.insertLanguage("Test lang", wordSet.getForeignLanguageCode()); //TODO: FIX LANG

                for (WordPair wp : wordSet.getWordPairs()) {
                    Word nativeWord = wp.getNativeWord();
                    Word foreignWord = wp.getForeignWord();

                    nativeWord.setLanguageID(nLangId);
                    foreignWord.setLanguageID(fLangId);

                    long pairID = mWordPairRepository.saveWordPair(nativeWord, foreignWord);
                    mPairWithSetDao.insert(new PairWithSet(setID, pairID));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public LiveData<List<Set>> getOnlineSets() {
        return mOnlineSets;
    }

    public LiveData<List<Set>> getAllSets() {
        return mAllSets;
    }

    public void uploadSet(Set set) {
        WordSet wordSet = new WordSet(set, getAllWordPairsInSet(set.getSetID()), "en", "fr");

        mFireBase.getReference().child("sets").push().setValue(wordSet);
    }

    public Set getSet(long setId) {
        return setDao.getSetByID(setId);
    }

    public void deleteSet(Set set) {
        setDao.delete(set);
    }

    public void saveSet(String name, String description, List<WordPair> wordPairs) {
        Set set = new Set(0, name, description);
        long setId = setDao.insert(set);

        for (WordPair wp : wordPairs) {
            mPairWithSetDao.insert(new PairWithSet(setId, wp.getPairID()));
        }
    }

    public List<WordPair> getAllWordPairsInSet(long setID) {
        return mPairWithSetDao.getAllWordPairsInSet(setID);
    }
}