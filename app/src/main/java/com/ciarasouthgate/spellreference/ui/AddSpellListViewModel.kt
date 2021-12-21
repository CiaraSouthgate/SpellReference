package com.ciarasouthgate.spellreference.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ciarasouthgate.spellreference.api.SpellApi
import com.ciarasouthgate.spellreference.database.Spell

class AddSpellListViewModel : SpellListViewModel() {
    private val _spellList = MutableLiveData<List<Spell>>(emptyList())
    override val spellList: LiveData<List<Spell>> = _spellList

    override val showAddButton = false

    override suspend fun fetchData(levels: List<Int>?) {
        _spellList.postValue(SpellApi.getSpells(if (levels.isNullOrEmpty()) null else levels))
    }
}