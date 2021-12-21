package com.ciarasouthgate.spellreference.ui

import androidx.lifecycle.*
import com.ciarasouthgate.spellreference.database.Spell
import com.ciarasouthgate.spellreference.database.SpellDatabaseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class SavedSpellListViewModel(
    private val dataSource: SpellDatabaseDao
) : SpellListViewModel() {
    override val showAddButton = true
    private val _spellList = MutableLiveData<List<Spell>>(emptyList())
    override val spellList: LiveData<List<Spell>> = _spellList

    override suspend fun fetchData(levels: List<Int>?) {
        _spellList.postValue(
            if (levels.isNullOrEmpty()) dataSource.getSaved() else dataSource.getSaved(levels)
        )
    }

    fun search(searchText: String) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _spellList.postValue(dataSource.search(searchText))
            } catch (e: Exception) {
                Timber.e(e, e.message)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}

class SavedSpellListViewModelFactory(private val dataSource: SpellDatabaseDao) :
    ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SavedSpellListViewModel::class.java)) {
            return SavedSpellListViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}