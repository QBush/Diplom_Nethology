package ru.netology.recipiesbook.Main.AdapterAndVMContentRecipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.db.AppDb
import ru.netology.recipiesbook.Main.data.MainRepository
import ru.netology.recipiesbook.Main.data.Recipe
import ru.netology.recipiesbook.Main.data.RecipeContent
import ru.netology.recipiesbook.Main.data.Repository
import ru.netology.recipiesbook.Main.utils.SingleLiveEvent

class RecipeContentViewModel(
    application: Application
) : AndroidViewModel(application), RecipeContentListener {

    //TODO здесь получает null
    private val repository: Repository = MainRepository(
        dao = AppDb.getInstance(context = application).postDao
    )

    // список с шагами, которые мы временно храним здесь
    val stepList = MutableLiveData<MutableList<RecipeContent>>(mutableListOf())

    val data by repository::data

    override fun onDeleteStepClick(stepNumber: Int) {
        stepList.value?.removeAll() { recipeStep ->
            recipeStep.stepNumber == stepNumber
        }
    }

    // TODO здесь нужно будет организовать добавление шагов перед сохранением в репо
    override fun onSaveButtonClick(recipe: Recipe) {
        repository.save(recipe)
    }

    override fun onSaveStepClick(recipeContent: RecipeContent) {
        stepList.value?.find { it.stepNumber == recipeContent.stepNumber }?.saved = !recipeContent.saved
    }
}