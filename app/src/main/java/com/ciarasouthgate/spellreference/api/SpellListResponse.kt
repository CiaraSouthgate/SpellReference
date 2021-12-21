package com.ciarasouthgate.spellreference.api

import com.ciarasouthgate.spellreference.database.Spell

data class SpellListResponse(
    val count: Int,
    val results: List<Spell>
)