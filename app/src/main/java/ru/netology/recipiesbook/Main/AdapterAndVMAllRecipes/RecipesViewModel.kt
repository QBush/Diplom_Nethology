package ru.netology.recipiesbook.Main.AdapterAndVMAllRecipes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.adapter.RecipeInteractionListener
import ru.netology.nmedia.db.AppDb
import ru.netology.recipiesbook.Main.data.MainRepository
import ru.netology.recipiesbook.Main.data.Recipe
import ru.netology.recipiesbook.Main.data.Repository
import ru.netology.recipiesbook.Main.data.Repository.Companion.NEW_RECIPE_ID
import ru.netology.recipiesbook.Main.utils.SingleLiveEvent

class RecipesViewModel(
    application: Application
) : AndroidViewModel(application), RecipeInteractionListener {

    private val repository: Repository = MainRepository(
        dao = AppDb.getInstance(context = application).postDao
    )

    val data by repository::data

    val navigateToSingleRecipeFragment = SingleLiveEvent<Long>()
    val navigateToRecipeContentFragmentFromAllRecipes = SingleLiveEvent<Long>()

    fun onAddClick() {
        navigateToRecipeContentFragmentFromAllRecipes.value = NEW_RECIPE_ID
    }

    override fun onEditClick(recipe: Recipe) {
        navigateToRecipeContentFragmentFromAllRecipes.value = recipe.recipeId
    }

    override fun onRemoveClick(recipeId: Long) = repository.delete(recipeId)

    override fun onContentClick(recipe: Recipe) {
        navigateToSingleRecipeFragment.value = recipe.recipeId
    }

    override fun onAddToFavoritesClick(recipeId: Long) = repository.addToFavorites(recipeId)


}