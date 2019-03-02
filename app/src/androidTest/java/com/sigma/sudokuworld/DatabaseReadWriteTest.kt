package com.sigma.sudokuworld

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.sigma.sudokuworld.persistence.db.*
import com.sigma.sudokuworld.persistence.db.Set

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

import java.io.IOException
import java.lang.Exception

@RunWith(AndroidJUnit4::class)
class DatabaseInstrumentedTest {
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getTargetContext()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).fallbackToDestructiveMigration().build()
        populateDatabase()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun readLanguage() {
        val storedLanguages = db.getLanguageDao().getAll()
        assertEquals("English", storedLanguages[0].name)
        assertEquals("en", storedLanguages[0].code)
        assertEquals("French", storedLanguages[1].name)
        assertEquals("fr", storedLanguages[1].code)
    }

    @Test
    @Throws(Exception::class)
    fun deleteLanguage() {
        val languageDao = db.getLanguageDao()

        //Delete all test
        languageDao.deleteAll()
        assertTrue(languageDao.getAll().isEmpty())
    }


    @Test
    @Throws(Exception::class)
    fun readWord() {
        val storedWords = db.getWordDao().getAll()
        assertEquals("en_word", storedWords[0].word)
        assertEquals(1, storedWords[0].languageID)
        assertEquals(1, storedWords[0].wordID)
        assertEquals("fr_word", storedWords[1].word)
        assertEquals(2, storedWords[1].languageID)
        assertEquals(2, storedWords[1].wordID)
    }

    @Test
    @Throws(Exception::class)
    fun readWordPair() {
        val storedWordPairs = db.getWordPairDao().getAll()
        assertEquals(1, storedWordPairs[0].wordPairID)
        assertEquals(1, storedWordPairs[0].nativeWordID)
        assertEquals(2, storedWordPairs[0].foreignWordID)
    }

    @Test
    @Throws(Exception::class)
    fun readSet() {
        val storedSets = db.getSetDao().getAll()
        assertEquals( 1, storedSets[0].setID)
        assertEquals("test_set_name", storedSets[0].name)
        assertEquals("test_set_description", storedSets[0].description)
    }

    /**
     * Test the cross reference table
     */
    @Test
    @Throws(Exception::class)
    fun readWordSet() {
        val wordPairsInSet = db.getWordSetDao().getAllWordsInSet(1)
        assertEquals(2, wordPairsInSet.size)
        assertEquals(1, wordPairsInSet[0].nativeWordID)
    }


    private fun populateDatabase() {
        val lang1 = Language(1, "English", "en")
        val lang2 = Language(2, "French", "fr")
        db.getLanguageDao().insert(lang1, lang2)

        val word1 = Word(1, 1, "en_word")
        val word2 = Word(2, 2, "fr_word")
        db.getWordDao().insert(word1, word2)

        val wordPair1 = WordPair(1, 1, 2)
        val wordPair2 = WordPair(2, 2, 1)
        db.getWordPairDao().insert(wordPair1, wordPair2)

        val set = Set(1, "test_set_name", "test_set_description")
        db.getSetDao().insert(set)

        val wordSet1 = WordSet(1, 1)
        val wordSet2 = WordSet(1, 2)
        db.getWordSetDao().insert(wordSet1, wordSet2)
    }
}