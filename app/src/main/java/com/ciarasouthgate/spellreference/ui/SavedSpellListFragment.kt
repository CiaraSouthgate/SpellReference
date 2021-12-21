package com.ciarasouthgate.spellreference.ui

import android.view.*
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.ciarasouthgate.spellreference.R
import com.ciarasouthgate.spellreference.database.Spell
import com.ciarasouthgate.spellreference.database.SpellDatabase
import com.ciarasouthgate.spellreference.databinding.SavedSpellListFragmentBinding
import com.ciarasouthgate.spellreference.databinding.SavedSpellListItemBinding
import com.ciarasouthgate.spellreference.util.toLevelOrdinal

class SavedSpellListFragment
    : SpellListFragment<SavedSpellListFragmentBinding, SavedSpellListViewModel>() {
    override lateinit var binding: SavedSpellListFragmentBinding
    override lateinit var adapter: SpellListAdapter

    override fun createViewModel(): SavedSpellListViewModel {
        val application = requireNotNull(this.activity).application
        val dataSource = SpellDatabase.getInstance(application).spellDatabaseDao
        val viewModelFactory = SavedSpellListViewModelFactory(dataSource)
        return ViewModelProvider(this, viewModelFactory)
            .get(SavedSpellListViewModel::class.java)
    }

    override fun setupBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): SavedSpellListFragmentBinding =
        SavedSpellListFragmentBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
            it.savedSpellRecyclerView.adapter = SavedSpellListAdapter().also { adapter = it }
            it.addButton.setOnClickListener {
                findNavController().navigate(R.id.action_savedSpellListFragment_to_addSpellListFragment)
            }
            it.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                val viewModel = this@SavedSpellListFragment.viewModel
                override fun onQueryTextSubmit(query: String?): Boolean = search(query)

                override fun onQueryTextChange(newText: String?): Boolean = search(newText)

                private fun search(query: String?): Boolean {
                    if (query.isNullOrBlank()) {
                        viewModel.getPageData()
                    } else {
                        viewModel.search(query)
                    }
                    return true
                }

            })
        }

    inner class SavedSpellListAdapter : SpellListFragment.SpellListAdapter() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpellListViewHolder {
            val binding = SavedSpellListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return SavedSpellListViewHolder(binding)
        }

        inner class SavedSpellListViewHolder(private val binding: SavedSpellListItemBinding) :
            SpellListAdapter.SpellListViewHolder(binding.root) {
            override fun bind(item: Spell) {
                binding.spell = item
                binding.level = item.level?.toLevelOrdinal(resources) ?: ""
                binding.root.setOnClickListener {
                    it.findNavController().navigate(
                        SavedSpellListFragmentDirections
                            .actionSavedSpellListFragmentToSpellDetailFragment(item)
                    )
                }
            }
        }
    }
}