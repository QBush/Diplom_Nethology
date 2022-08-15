package ru.netology.recipiesbook.Main.AdapterAndVMContentRecipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.netology.nmedia.db.AppDb
import ru.netology.recipiesbook.Main.data.MainRepository
import ru.netology.recipiesbook.Main.data.Repository
import ru.netology.recipiesbook.Main.utils.SingleLiveEvent

class RecipeContentViewModel(
    application: Application
) : AndroidViewModel(application), RecipeContentListener {

    val deleteStepEvent = SingleLiveEvent<Int>()

    private val repository: Repository = MainRepository(
        dao = AppDb.getInstance(context = application).postDao
    )
    val data by repository::data


    override fun onDeleteStepClick(stepNumber: Int) {
        deleteStepEvent.value = stepNumber
    }
}