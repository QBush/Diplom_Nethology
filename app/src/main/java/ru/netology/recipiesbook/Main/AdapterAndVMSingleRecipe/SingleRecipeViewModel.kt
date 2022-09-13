package ru.netology.recipiesbook.Main.AdapterAndVMSingleRecipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.adapter.RecipeInteractionListener
import ru.netology.nmedia.db.AppDb
import ru.netology.recipiesbook.Main.data.MainRepository
import ru.netology.recipiesbook.Main.data.Recipe
import ru.netology.recipiesbook.Main.data.Repository
import ru.netology.recipiesbook.Main.utils.SingleLiveEvent

class SingleRecipeViewModel(
    application: Application
) : AndroidViewModel(application), RecipeInteractionListener {

    private val repository: Repository = MainRepository(
        dao = AppDb.getInstance(context = application).postDao
    )

    val data by repository::data

    var currentRecipe = MutableLiveData<Recipe>(null)

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