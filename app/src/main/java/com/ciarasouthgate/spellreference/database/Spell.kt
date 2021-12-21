package com.ciarasouthgate.spellreference.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "spell")
@Parcelize
data class Spell(
    @PrimaryKey
    val index: String,
    val name: String,
    val url: String,
    val description: String?,
    val higherLevel: String?,
    val range: String?,
    val components: String?,
    val material: String?,
    val duration: String?,
    val concentration: Boolean?,
    val castingTime: String?,
    val level: Int?,
    val attackType: String?,
    val damageType: String?,
    val school: String?,
    val classes: String?,
    var isFavourite: Boolean = false
) : Parcelable {
    val info get() = listOfNotNull(school, components).joinToString(" • ")
}