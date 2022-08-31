package ru.netology.recipiesbook.Main

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.edit
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.netology.recipiesbook.Main.data.Recipe
import ru.netology.recipiesbook.R

class FilterDialogFragment : DialogFragment() {
    companion object {
        const val SAVED_CHECKBOXES = "checkboxes"
        const val FILTER_DIALOG_RESULT = "filter_dialog_result"
        const val SAVED_CATEGORIES_KEY = "categories"
    }

    // достаем значение чекбоксов предыдущее
    private val previousContent = context?.getSharedPreferences(
        "previousNewContent", Context.MODE_PRIVATE
    )
    val content = previousContent?.getString(SAVED_CHECKBOXES, null)
    val checkedItems: BooleanArray = if (content != null) {
        Json.decodeFromString(content)
    } else booleanArrayOf(true, true, true, true, true, true, true)

    private val categories = resources.getStringArray(R.array.categories_array)
    private val chosenCategories = arrayListOf<String>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Choose categories")
                .setMultiChoiceItems(categories, checkedItems) { _, which, isChecked ->
                    checkedItems[which] = isChecked
                    if (isChecked) {
                        chosenCategories.add(categories[which])
                    }
                }
                .setPositiveButton(R.string.ok)
                { dialog, id ->
                    setFragmentResult(SAVED_CATEGORIES_KEY, bundleOf("bundle" to chosenCategories))
                    Json.encodeToString(checkedItems)
//                    val direction = FilterDialogFragmentDirections.toAllRecipesFragment(Json.encodeToString(choosenCategories))
//                    findNavController().navigate(direction)
                }
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    setFragmentResult(FILTER_DIALOG_RESULT, bundleOf(SAVED_CATEGORIES_KEY to categories))
//                    val direction = FilterDialogFragmentDirections.toAllRecipesFragment(Json.encodeToString(categories))
//                    findNavController().navigate(direction)
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}

