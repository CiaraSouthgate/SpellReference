package com.ciarasouthgate.spellreference.ui

import androidx.lifecycle.*
import com.ciarasouthgate.spellreference.api.SpellApi
import com.ciarasouthgate.spellreference.database.Spell
import com.ciarasouthgate.spellreference.database.SpellDatabaseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class SpellDetailViewModel(
    private val dataSource: SpellDatabaseDao,
    private val receivedSpell: Spell
) : ViewModel() {
    private val _spell = MutableLiveData(receivedSpell)
    val spell: LiveData<Spell> = _spell

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        if (receivedSpell.description == null) {
            getFullSpellDetail()
        }
    }

    private fun getFullSpellDetail() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = dataSource.get(receivedSpell.index)
                ?: SpellApi.getSpell(receivedSpell.index)
            _spell.postValue(result)
            _isLoading.postValue(false)
        }
    }

    fun toggleFavourite(): Boolean =
        _spell.value?.let {
            it.isFavourite = !it.isFavourite
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    if (it.isFavourite) {
                        dataSource.insert(it)
                    } else {
                        dataSource.delete(it.index)
                    }
                } catch (e: Exception) {
                    Timber.e(e, e.message)
                }
            }
            it.isFavourite
        } ?: false
}

class SpellDetailViewModelFactory(
    private val dataSource: SpellDatabaseDao,
    private val spell: Spell
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SpellDetailViewModel::class.java)) {
            return SpellDetailViewModel(dataSource, spell) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}