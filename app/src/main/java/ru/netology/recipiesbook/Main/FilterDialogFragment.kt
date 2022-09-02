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

//фрагмент для фильтрации по категориям
class FilterDialogFragment : DialogFragment() {
    companion object {
        const val SAVED_CHECKBOXES = "checkboxes"
        const val FILTER_DIALOG_RESULT = "filter_dialog_result"
        const val SAVED_CATEGORIES_KEY = "categories"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // достаем значение чекбоксов предыдущее
        val previousContent = context?.getSharedPreferences(
            "previousNewContent", Context.MODE_PRIVATE
        )
        val content = previousContent?.getString(SAVED_CHECKBOXES, null)
        val checkedItems: BooleanArray = if (content != null) {
            Json.decodeFromString(content)
        } else booleanArrayOf(true, true, true, true, true, true, true)

        val categories = resources.getStringArray(R.array.categories_array)
        val chosenCategories = arrayListOf<String>()

        //получаем предыдущее состояние чекбоксов
        for (i in categories.indices) {
            if (checkedItems[i]) {
                chosenCategories.add(categories[i])
            }
        }
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Choose categories")
                .setMultiChoiceItems(categories, checkedItems) { _, which, isChecked ->
                    checkedItems[which] = isChecked
                    if (isChecked) {
                        chosenCategories.add(categories[which])
                    } else chosenCategories.remove(categories[which])
                }
                .setPositiveButton(R.string.ok)
                { dialog, id ->
                    previousContent?.edit {
                        putString(SAVED_CHECKBOXES, Json.encodeToString(checkedItems))
                    }
                    setFragmentResult(FILTER_DIALOG_RESULT, bundleOf(SAVED_CATEGORIES_KEY to chosenCategories))
//                    val direction = FilterDialogFragmentDirections.toAllRecipesFragment(Json.encodeToString(choosenCategories))
//                    findNavController().navigate(direction)
                }
                .setNegativeButton(R.string.cancel) { dialog, _ ->
//                    setFragmentResult(FILTER_DIALOG_RESULT, bundleOf(SAVED_CATEGORIES_KEY to categories))
//                    val direction = FilterDialogFragmentDirections.toAllRecipesFragment(Json.encodeToString(categories))
//                    findNavController().navigate(direction)
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}

