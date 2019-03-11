package com.sigma.sudokuworld

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.sigma.sudokuworld.game.GameDifficulty
import com.sigma.sudokuworld.game.GameMode
import com.sigma.sudokuworld.persistence.db.*
import com.sigma.sudokuworld.persistence.db.entities.*
import com.sigma.sudokuworld.persistence.db.entities.Set

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
    private lateinit var languageList: List<Language>
    private lateinit var wordList: List<Word>
    private lateinit var wordPairList: List<Pair>
    private lateinit var setList: List<Set>
    private lateinit var mPairWithSetList: List<PairWithSet>

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
        assertEquals(languageList[0].name, storedLanguages[0].name)
        assertEquals(languageList[0].code, storedLanguages[0].code)
        assertEquals(languageList[1].name, storedLanguages[1].name)
        assertEquals(languageList[1].code, storedLanguages[1].code)
        assertNotEquals(languageList[1].name, storedLanguages[0].name)
        assertNotEquals(languageList[1].code, storedLanguages[0].code)
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
        assertEquals(wordList[0].word, storedWords[0].word)
        assertEquals(wordList[0].languageID, storedWords[0].languageID)
        assertEquals(wordList[0].wordID, storedWords[0].wordID)
        assertEquals(wordList[1].word, storedWords[1].word)
        assertEquals(wordList[1].languageID, storedWords[1].languageID)
        assertEquals(wordList[1].wordID, storedWords[1].wordID)
        assertNotEquals(wordList[1].word, storedWords[0].word)
        assertNotEquals(wordList[1].languageID, storedWords[0].languageID)
        assertNotEquals(wordList[1].wordID, storedWords[0].wordID)
    }

    @Test
    @Throws(Exception::class)
    fun readWordPair() {
        val storedWordPairs = db.getWordPairDao().getAll()
        assertEquals(wordPairList[0].pairID, storedWordPairs[0].pairID)
        assertEquals(wordPairList[0].nativeWordID, storedWordPairs[0].nativeWordID)
        assertEquals(wordPairList[0].foreignWordID, storedWordPairs[0].foreignWordID)
        assertEquals(wordPairList[1].pairID, storedWordPairs[1].pairID)
        assertEquals(wordPairList[1].nativeWordID, storedWordPairs[1].nativeWordID)
        assertEquals(wordPairList[1].foreignWordID, storedWordPairs[1].foreignWordID)
        assertNotEquals(wordPairList[1].pairID, storedWordPairs[0].pairID)
    }

    @Test
    @Throws(Exception::class)
    fun readSet() {
        val storedSets = db.getSetDao().getAll()
        assertEquals( setList[0].setID, storedSets[0].setID)
        assertEquals(setList[0].name, storedSets[0].name)
        assertEquals(setList[0].description, storedSets[0].description)
    }

    /**
     * Test the cross reference table
     */
    @Test
    @Throws(Exception::class)
    fun readWordSet() {
        val wordPairsInSet = db.getWordSetDao().getAllWordPairsInSet(1)
        assertEquals(2, wordPairsInSet.size)
        assertEquals(1, wordPairsInSet[0].nativeWord.wordID)
    }

    @Test
    @Throws(Exception::class)
    fun readGameSave() {

        val save1 = Game(
                1,
                1,
                GameDifficulty.EASY,
                GameMode.NUMBERS,
                IntArray(0),
                IntArray(0),
                BooleanArray(0))
        val save2 = Game(
                2,
                1,
                GameDifficulty.MEDIUM,
                GameMode.NATIVE,
                intArrayOf(1, 2, 3),
                intArrayOf(1, 3, 3),
                booleanArrayOf(true, false, true))

        val gameList = listOf(save1, save2)

        db.getGameSaveDao().insert(save1)
        db.getGameSaveDao().insert(save2)

        val gameSaves = db.getGameSaveDao().getAllStatic()
        assertEquals(gameList[0].saveID, gameSaves[0].saveID)
        assertEquals(gameList[0].difficulty, gameSaves[0].difficulty)
        assertEquals(gameList[0].gameMode, gameSaves[0].gameMode)
        assertTrue(gameList[0].cellValues.contentEquals(gameSaves[0].cellValues))
        assertTrue(gameList[0].solutionValues.contentEquals(gameSaves[0].solutionValues))
        assertTrue(gameList[0].lockedCells.contentEquals(gameSaves[0].lockedCells))

        assertEquals(gameList[1].saveID, gameSaves[1].saveID)
        assertEquals(gameList[1].difficulty, gameSaves[1].difficulty)
        assertEquals(gameList[1].gameMode, gameSaves[1].gameMode)
        assertTrue(gameList[1].cellValues.contentEquals(gameSaves[1].cellValues))
        assertTrue(gameList[1].solutionValues.contentEquals(gameSaves[1].solutionValues))
        assertTrue(gameList[1].lockedCells.contentEquals(gameSaves[1].lockedCells))

        assertNotEquals(gameList[0].saveID, gameSaves[1].saveID)

        //Testing overridden equals() and hashcode()
        assertEquals(gameList[0], gameSaves[0])
        assertEquals(gameList[1], gameSaves[1])
        assertNotEquals(gameList[0], gameSaves[1])
        assertEquals(gameList[0].hashCode(), gameSaves[0].hashCode())
        assertEquals(gameList[1].hashCode(), gameSaves[1].hashCode())
        assertNotEquals(gameList[0].hashCode(), gameSaves[1].hashCode())

        //Testing array getters
        assertEquals(gameList[1].cellValues[1], gameList[1].getCellValue(1))
        assertEquals(gameList[1].lockedCells[0], gameList[1].isLocked(0))
        assertEquals(gameList[1].lockedCells[1], gameList[1].isLocked(1))
    }


    private fun populateDatabase() {

        val lang1 = Language(1, "English", "en")
        val lang2 = Language(2, "French", "fr")
        languageList = listOf(lang1, lang2)
        db.getLanguageDao().insert(lang1, lang2)

        val word1 = Word(1, 1, "en_word")
        val word2 = Word(2, 2, "fr_word")
        wordList = listOf(word1, word2)
        db.getWordDao().insert(word1, word2)

        val wordPair1 = Pair(1, 1, 2)
        val wordPair2 = Pair(2, 2, 1)
        wordPairList = listOf(wordPair1, wordPair2)
        db.getWordPairDao().insert(wordPair1, wordPair2)

        val set1 = Set(1, "test_set_name", "test_set_description")
        setList = listOf(set1)
        db.getSetDao().insert(set1)

        val wordSet1 = PairWithSet(1, 1)
        val wordSet2 = PairWithSet(1, 2)
        mPairWithSetList = listOf(wordSet1, wordSet2)
        db.getWordSetDao().insert(wordSet1, wordSet2)
    }
}