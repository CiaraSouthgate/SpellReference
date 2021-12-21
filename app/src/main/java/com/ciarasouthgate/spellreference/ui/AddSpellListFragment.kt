package com.ciarasouthgate.spellreference.ui


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.ciarasouthgate.spellreference.database.Spell
import com.ciarasouthgate.spellreference.databinding.AddSpellListFragmentBinding
import com.ciarasouthgate.spellreference.databinding.AddSpellListItemBinding

class AddSpellListFragment :
    SpellListFragment<AddSpellListFragmentBinding, AddSpellListViewModel>() {
    override lateinit var binding: AddSpellListFragmentBinding
    override lateinit var adapter: SpellListAdapter

    override fun createViewModel(): AddSpellListViewModel {
        val model: AddSpellListViewModel by viewModels()
        return model
    }

    override fun setupBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): AddSpellListFragmentBinding =
        AddSpellListFragmentBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
            it.addSpellRecyclerView.adapter = AddSpellListAdapter().also { adapter = it }
        }

    class AddSpellListAdapter : SpellListFragment.SpellListAdapter() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpellListViewHolder {
            val binding = AddSpellListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return AddSpellListViewHolder(binding)
        }

        class AddSpellListViewHolder(private val binding: AddSpellListItemBinding) :
            SpellListAdapter.SpellListViewHolder(binding.root) {
            override fun bind(item: Spell) {
                binding.spellName = item.name
                binding.root.setOnClickListener {
                    it.findNavController().navigate(
                        AddSpellListFragmentDirections
                            .actionAddSpellListFragmentToSpellDetailFragment(item)
                    )
                }
            }
        }
    }
}