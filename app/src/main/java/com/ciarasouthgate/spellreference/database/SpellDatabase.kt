package com.ciarasouthgate.spellreference.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Spell::class], version = 12)
abstract class SpellDatabase : RoomDatabase() {
    abstract val spellDatabaseDao: SpellDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: SpellDatabase? = null

        fun getInstance(context: Context): SpellDatabase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    SpellDatabase::class.java,
                    "spell_database"
                ).fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}