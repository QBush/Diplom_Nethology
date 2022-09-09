package ru.netology.recipiesbook.Main.AdapterAndVMContentRecipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.db.AppDb
import ru.netology.recipiesbook.Main.data.MainRepository
import ru.netology.recipiesbook.Main.data.Recipe
import ru.netology.recipiesbook.Main.data.RecipeContent
import ru.netology.recipiesbook.Main.data.Repository
import ru.netology.recipiesbook.Main.updateRecipeStepsNumbers
import ru.netology.recipiesbook.Main.utils.SingleLiveEvent

class RecipeContentViewModel(
    application: Application
) : AndroidViewModel(application), RecipeContentListener {

    private val repository: Repository = MainRepository(
        dao = AppDb.getInstance(context = application).postDao
    )

    val data by repository::data

    // список с шагами, которые мы временно храним здесь
    val stepList = MutableLiveData<MutableList<RecipeContent>>(mutableListOf())


    override fun onDeleteStepClick(stepNumber: Int) {

        stepList.value?.removeAll { recipeStep ->
            recipeStep.stepNumber == stepNumber
        }
        updateRecipeStepsNumbers(stepList.value)
        stepList.value = stepList.value?.toMutableList()
    }

    override fun onSaveButtonClick(recipe: Recipe) {
        repository.save(recipe)
    }

//TODO метод сохранения (утверждения)шага. Работает, адаптер не обновляется
    override fun onSaveStepClick(recipeContent: RecipeContent) {
        stepList.value?.replaceAll {
            when (it.stepNumber) {
                recipeContent.stepNumber -> {
                    recipeContent.copy(saved = !recipeContent.saved)
                }
                else -> {
                    it
                }
            }
        }
        stepList.value = stepList.value?.toMutableList()
    }
}