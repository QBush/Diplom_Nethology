package ru.netology.recipiesbook.Main.AdapterAndVMSingleRecipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.netology.nmedia.adapter.RecipeInteractionListener
import ru.netology.nmedia.db.AppDb
import ru.netology.recipiesbook.Main.data.MainRepository
import ru.netology.recipiesbook.Main.data.Recipe
import ru.netology.recipiesbook.Main.data.Repository
import ru.netology.recipiesbook.Main.utils.SingleLiveEvent

class SingleRecipeViewModel(
    application: Application
): AndroidViewModel(application), RecipeInteractionListener {

    //TODO здесь получает null
    private val repository: Repository = MainRepository(
        dao = AppDb.getInstance(context = application).postDao
    )
    //TODO здесь получает null
    val data by repository::data

    val navigateToRecipeContentFragmentFromSingleRecipe = SingleLiveEvent<Long>()

    override fun onEditClick(recipe: Recipe) {
        navigateToRecipeContentFragmentFromSingleRecipe.value = recipe.recipeId
    }

    override fun onRemoveClick(recipeId: Long) = repository.delete(recipeId)

    override fun onContentClick(recipe: Recipe) {
        // nothing to do
    }

    override fun onAddToFavoritesClick(recipeId: Long) = repository.addToFavorites(recipeId)

}