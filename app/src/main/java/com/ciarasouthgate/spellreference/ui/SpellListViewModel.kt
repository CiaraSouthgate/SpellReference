package com.ciarasouthgate.spellreference.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ciarasouthgate.spellreference.database.Spell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class SpellListViewModel : ViewModel() {
    abstract val spellList: LiveData<List<Spell>>
    abstract val showAddButton: Boolean

    protected val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _selectedLevels = MutableLiveData<List<Int>>(emptyList())
    val selectedLevels: LiveData<List<Int>> = _selectedLevels

    init {
        selectedLevels.observeForever {
            getPageData()
        }
    }

    fun getPageData() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                fetchData(selectedLevels.value)
            } catch (e: Exception) {
                Timber.e(e, e.message)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun updateSelectedLevels(levels: List<Int>) {
        _selectedLevels.value = levels
    }

    protected abstract suspend fun fetchData(levels: List<Int>?)
}