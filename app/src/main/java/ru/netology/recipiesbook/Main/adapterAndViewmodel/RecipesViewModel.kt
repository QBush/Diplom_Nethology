package ru.netology.recipiesbook.Main.adapterAndViewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.adapter.RecipeInteractionListener
import ru.netology.recipiesbook.Main.data.MainRepository
import ru.netology.recipiesbook.Main.data.Recipe
import ru.netology.recipiesbook.Main.data.Repository
import ru.netology.recipiesbook.Main.utils.SingleLiveEvent

class RecipesViewModel(
    application: Application
) : AndroidViewModel(application), RecipeInteractionListener {

    private val repository: Repository = MainRepository(application)

    val data by repository::data

    val navigateToNewRecipeFragment = SingleLiveEvent<String>()
    private val currentRecipe = MutableLiveData<Recipe?>(null)

    fun onAddClick() {
        navigateToNewRecipeFragment.call()
    }

    fun onSaveButtonClick(content: String) {
        if (content.isBlank()) return
        val recipe = currentRecipe.value?.copy(
            content = content) ?: Recipe(
            recipeId = Repository.NEW_RECIPE_ID,
            author = "me",
            content = content,
            published = "01.08.2022"
        )
        repository.save(recipe)
        currentRecipe.value = null
    }

    override fun onRemoveClick(recipe: Recipe) {
        TODO("Not yet implemented")
    }

    override fun onEditClick(recipe: Recipe) {
        TODO("Not yet implemented")
    }

    override fun onContentClick(recipe: Recipe) {
        TODO("Not yet implemented")
    }

}