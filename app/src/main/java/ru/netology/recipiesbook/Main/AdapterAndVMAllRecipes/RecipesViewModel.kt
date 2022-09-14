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

    // событие перехода на одиночный фрагмент
    val navigateToSingleRecipeFragment = SingleLiveEvent<Long>()

    // событие перехода на фрагмент редактирования
    val navigateToRecipeContentFragmentFromAllRecipes = SingleLiveEvent<Long>()

    //Отфильтрованный список во фрагменте со всеми рецептами
    var filteredRecipeList = MutableLiveData<List<Recipe>?>()

    //Отфильтрованный список во фрагменте с избранными рецептами
    var filteredFavoriteRecipeList = MutableLiveData<List<Recipe>?>()

    //хранит для двух фрагментов текущую фильтрацию
    var choosenFilteredCategories = MutableLiveData<ArrayList<String>?>()

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
        val filteredRecipes = data.value?.toMutableList() ?: return null
        val resultList = ArrayList<Recipe>()
        for (recipe in filteredRecipes) {
            if (recipe.recipeName.toLowerCase().contains(text.toLowerCase())
            ) {
                resultList.add(recipe)
            }
        }
        return resultList
    }

    fun filterFavorite(text: String): MutableList<Recipe>? {
        val filteredRecipes = data.value?.toMutableList() ?: return null
        val resultList = ArrayList<Recipe>()
        for (recipe in filteredRecipes) {
            if (recipe.recipeName.toLowerCase().contains(text.toLowerCase())
            ) {
                resultList.add(recipe)
            }
        }
        return resultList
    }

}