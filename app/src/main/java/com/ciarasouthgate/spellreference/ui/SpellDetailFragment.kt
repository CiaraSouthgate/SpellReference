package com.ciarasouthgate.spellreference.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.ciarasouthgate.spellreference.R
import com.ciarasouthgate.spellreference.database.Spell
import com.ciarasouthgate.spellreference.database.SpellDatabase
import com.ciarasouthgate.spellreference.databinding.SpellDetailFragmentBinding
import com.ciarasouthgate.spellreference.databinding.SpellStatViewBinding
import com.ciarasouthgate.spellreference.util.toLevelOrdinal
import kotlin.math.ceil

class SpellDetailFragment : Fragment() {
    private lateinit var binding: SpellDetailFragmentBinding
    private var viewModel: SpellDetailViewModel? = null

    private val args: SpellDetailFragmentArgs by navArgs()

    private val spellObserver = Observer<Spell> {
        updateUI(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val application = requireNotNull(this.activity).application
        val dataSource = SpellDatabase.getInstance(application).spellDatabaseDao
        val viewModelFactory = SpellDetailViewModelFactory(dataSource, args.spell)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(SpellDetailViewModel::class.java).also {
                it.spell.observe(viewLifecycleOwner, spellObserver)
            }
        binding = SpellDetailFragmentBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
            it.favouriteIcon.setOnClickListener {
                val isFavourite = viewModel?.toggleFavourite() ?: false
                updateFavouriteIcon(isFavourite)
            }
        }
        updateFavouriteIcon(args.spell.isFavourite)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel?.spell?.removeObserver(spellObserver)
    }

    private fun updateUI(spell: Spell) {
        with(spell) {
            higherLevel.isNullOrBlank().let {
                binding.higherLevelTitle.isVisible = !it
                binding.higherLevelText.isVisible = !it
            }
            material.isNullOrBlank().let {
                binding.materialTitle.isVisible = !it
                binding.materialText.isVisible = !it
            }
            classes.isNullOrBlank().let {
                binding.classesTitle.isVisible = !it
                binding.classesText.isVisible = !it
            }
        }
        populateStats(spell)
        updateFavouriteIcon(spell.isFavourite)
    }

    private fun populateStats(spell: Spell) {
        val stats = mutableListOf<SpellStat>()
        with(spell) {
            level?.let { addStat(stats, R.string.level, it.toLevelOrdinal(resources)) }
            if (!school.isNullOrBlank()) addStat(stats, R.string.school, school)
            if (!duration.isNullOrBlank()) addStat(stats, R.string.duration, duration)
            if (!castingTime.isNullOrBlank()) addStat(stats, R.string.casting_time, castingTime)
            if (!components.isNullOrBlank()) addStat(stats, R.string.components, components)
            if (!range.isNullOrBlank()) addStat(stats, R.string.range, range)
            concentration?.let {
                addStat(
                    stats, R.string.concentration,
                    if (it) getString(R.string.yes) else getString(R.string.no)
                )
            }
            if (!attackType.isNullOrBlank()) addStat(stats, R.string.attack_type, attackType)
            if (!damageType.isNullOrBlank()) addStat(stats, R.string.damage_type, damageType)
        }
        val grid = binding.detailAbilitiesGrid
        val inflater = LayoutInflater.from(context)
        val numColumns = grid.columnCount
        grid.rowCount = ceil(stats.size / numColumns.toFloat()).toInt()
        stats.forEachIndexed { index, stat ->
            val statBinding = SpellStatViewBinding.inflate(inflater, grid, false).apply {
                this.stat = stat
                this.lifecycleOwner = viewLifecycleOwner
            }
            val layoutParams = GridLayout.LayoutParams().apply {
                height = GridLayout.LayoutParams.WRAP_CONTENT
                width = GridLayout.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(index % numColumns, 1F)
                rowSpec = GridLayout.spec(index / numColumns, 1F)
            }
            grid.addView(statBinding.root, layoutParams)
        }
    }

    private fun addStat(list: MutableList<SpellStat>, stringId: Int, value: String) {
        list.add(SpellStat(getString(stringId), value))
    }

    private fun updateFavouriteIcon(isFavourite: Boolean) {
        val imageId =
            if (isFavourite) R.drawable.ic_favourite_filled else R.drawable.ic_favourite_border
        binding.favouriteIcon.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                imageId,
                requireContext().theme
            )
        )
    }

}