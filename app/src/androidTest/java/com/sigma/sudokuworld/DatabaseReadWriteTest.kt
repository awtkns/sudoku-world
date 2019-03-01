package com.sigma.sudokuworld

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4

import com.sigma.sudokuworld.db.AppDatabase
import com.sigma.sudokuworld.db.Language
import com.sigma.sudokuworld.db.LanguageDao

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
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getTargetContext()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).fallbackToDestructiveMigration().build()
        languageDao = db.getLanguageDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeLanguageAndRead() {
        val lang1 = Language(0, "English", "en")
        val lang2 = Language(0, "French", "fr")
        languageDao.insert(lang1, lang2)

        val storedLanguages = languageDao.getAll()
        assertEquals("English", storedLanguages[0].language)
        assertEquals("en", storedLanguages[0].code)
        assertEquals("French", storedLanguages[1].language)
        assertEquals("fr", storedLanguages[1].code)
    }
}