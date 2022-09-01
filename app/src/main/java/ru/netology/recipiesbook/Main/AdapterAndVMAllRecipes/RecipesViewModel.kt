package ru.netology.recipiesbook.Main.AdapterAndVMAllRecipes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
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

    var filteredRecipeList = MutableLiveData<List<Recipe>?>()
    var filteredFavoriteRecipeList = MutableLiveData<List<Recipe>?>()

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

    fun filter(text: String): MutableList<Recipe>? {
        val filteredRecipes = filteredRecipeList.value?.toMutableList() ?: data.value?.toMutableList() ?: return null
        for (recipe in filteredRecipes) {
            if (recipe.recipeName.toLowerCase().contains(text.toLowerCase())
            ) {
                filteredRecipes.add(recipe)
            }
        }
        return filteredRecipes
    }

    fun filterFavorite(text: String): MutableList<Recipe>? {
        val filteredRecipes = filteredFavoriteRecipeList.value?.toMutableList() ?: data.value?.toMutableList() ?: return null
        for (recipe in filteredRecipes) {
            if (recipe.recipeName.toLowerCase().contains(text.toLowerCase())
            ) {
                filteredRecipes.add(recipe)
            }
        }
        return filteredRecipes
    }

}