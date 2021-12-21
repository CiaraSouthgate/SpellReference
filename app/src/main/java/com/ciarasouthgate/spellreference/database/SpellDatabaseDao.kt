package com.ciarasouthgate.spellreference.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SpellDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(spell: Spell)

    @Query("SELECT * FROM spell WHERE `index` = :index")
    suspend fun get(index: String): Spell?

    @Query("DELETE FROM spell WHERE `index` = :index")
    suspend fun delete(index: String)

    @Query("SELECT * FROM spell WHERE isFavourite = 1 ORDER BY level ASC")
    suspend fun getSaved(): List<Spell>

    @Query("SELECT * FROM spell WHERE level IN (:levels) AND isFavourite = 1 ORDER BY level ASC")
    suspend fun getSaved(levels: List<Int>): List<Spell>

    @Query("SELECT * FROM spell WHERE name LIKE '%:searchText%' OR description LIKE '%' || :searchText || '%' ORDER BY level ASC")
    suspend fun search(searchText: String): List<Spell>
}