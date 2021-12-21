package com.ciarasouthgate.spellreference.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.ciarasouthgate.spellreference.R

class LevelSelectorFragment(
    private val initialLevels: List<Int>,
    private val onApply: (List<Int>) -> Unit
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val selected = BooleanArray(10) { index ->
                index in initialLevels
            }
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.filter_level))
                .setMultiChoiceItems(R.array.ordinals, selected) { _, index, isChecked ->
                    selected[index] = isChecked
                }
                .setPositiveButton(getString(R.string.apply)) { _, _ ->
                    onApply(selected.toList().mapIndexedNotNull { index, selected ->
                        if (selected) index else null
                    })
                }
                .setNegativeButton(getString(R.string.cancel)) { _, _ ->
                    // no op
                }
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        private const val TAG = "LevelSelectorFragment"
    }
}