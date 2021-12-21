package com.ciarasouthgate.spellreference.api

import com.ciarasouthgate.spellreference.database.Spell
import com.squareup.moshi.FromJson

class SpellAdapter {
    @FromJson
    fun spellFromJson(spellJson: SpellJson): Spell = with(spellJson) {
        Spell(
            index,
            name,
            url,
            desc?.joinToString("\n\n"),
            higher_level?.joinToString("\n\n"),
            range,
            components?.joinToString(),
            material,
            duration,
            concentration,
            casting_time,
            level,
            attack_type,
            damage?.damage_type?.name,
            school?.name,
            classes?.map { it.name }?.joinToString()
        )
    }

    data class SpellJson(
        val index: String,
        val name: String,
        val url: String,
        val desc: List<String>?,
        val higher_level: List<String>?,
        val range: String?,
        val components: List<String>?,
        val material: String?,
        val ritual: Boolean?,
        val duration: String?,
        val concentration: Boolean?,
        val casting_time: String?,
        val level: Int?,
        val attack_type: String?,
        val damage: Damage?,
        val school: Reference?,
        val classes: List<Reference>?
    )

    data class Damage(val damage_type: Reference)
    data class Reference(val name: String)
}