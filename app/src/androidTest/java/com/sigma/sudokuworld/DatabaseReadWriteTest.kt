package com.sigma.sudokuworld

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.sigma.sudokuworld.db.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

import java.io.IOException
import java.lang.Exception

@RunWith(AndroidJUnit4::class)
class DatabaseReadWriteTest {
    private lateinit var languageDao: LanguageDao
    private lateinit var wordDao: WordDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getTargetContext()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).fallbackToDestructiveMigration().build()
        languageDao = db.getLanguageDao()
        wordDao = db.getWordDao()
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
        val storedLanguages = languageDao.getAll()
        assertEquals("English", storedLanguages[0].name)
        assertEquals("en", storedLanguages[0].code)
        assertEquals("French", storedLanguages[1].name)
        assertEquals("fr", storedLanguages[1].code)
    }

    @Test
    @Throws(Exception::class)
    fun readWord() {
        val storedWords = wordDao.getAll()
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

    private fun populateDatabase() {
        val lang1 = Language(1, "English", "en")
        val lang2 = Language(2, "French", "fr")
        languageDao.insert(lang1, lang2)

        val word1 = Word(1, 1, "en_word")
        val word2 = Word(2, 2, "fr_word")
        wordDao.insert(word1, word2)

        val wordPair = WordPair(1, 1, 2)
        db.getWordPairDao().insert(wordPair)
    }
}