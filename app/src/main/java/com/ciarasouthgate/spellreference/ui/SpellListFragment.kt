package com.ciarasouthgate.spellreference.ui

import android.os.Bundle
import android.view.*
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ciarasouthgate.spellreference.R
import com.ciarasouthgate.spellreference.database.Spell

abstract class SpellListFragment<BindingType : ViewDataBinding, ViewModel : SpellListViewModel> :
    Fragment() {
    protected lateinit var viewModel: ViewModel

    protected abstract var adapter: SpellListAdapter
    protected abstract var binding: BindingType

    private val spellListObserver = Observer<List<Spell>> {
        adapter.submitList(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = createViewModel().also {
            it.spellList.observe(viewLifecycleOwner, spellListObserver)
        }

        binding = setupBinding(inflater, container)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.getPageData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.spellList.removeObserver(spellListObserver)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_filter_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_filter -> {
                LevelSelectorFragment(viewModel.selectedLevels.value ?: emptyList()) {
                    viewModel.updateSelectedLevels(it)
                }.show(requireActivity().supportFragmentManager, "SpellListFragment")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    abstract fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): BindingType

    abstract fun createViewModel(): ViewModel

    abstract class SpellListAdapter : ListAdapter<Spell, SpellListAdapter.SpellListViewHolder>(
        SpellDiffCallback()
    ) {
        override fun onBindViewHolder(holder: SpellListViewHolder, position: Int) {
            val spell = getItem(position)
            holder.bind(spell)
        }

        abstract class SpellListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            abstract fun bind(item: Spell)
        }

        class SpellDiffCallback : DiffUtil.ItemCallback<Spell>() {
            override fun areItemsTheSame(oldItem: Spell, newItem: Spell): Boolean =
                oldItem.index == newItem.index

            override fun areContentsTheSame(oldItem: Spell, newItem: Spell): Boolean =
                oldItem == newItem
        }
    }
}