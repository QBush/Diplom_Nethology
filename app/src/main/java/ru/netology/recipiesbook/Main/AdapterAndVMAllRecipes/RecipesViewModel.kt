package ru.netology.recipiesbook.Main.AdapterAndVMAllRecipes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.adapter.RecipeInteractionListener
import ru.netology.nmedia.db.AppDb
import ru.netology.recipiesbook.Main.data.MainRepository
import ru.netology.recipiesbook.Main.data.Recipe
import ru.netology.recipiesbook.Main.data.Repository
import ru.netology.recipiesbook.Main.utils.SingleLiveEvent

class RecipesViewModel(
    application: Application
) : AndroidViewModel(application), RecipeInteractionListener {

    private val repository: Repository = MainRepository(
        dao = AppDb.getInstance(context = application).postDao
    )

    val data by repository::data

    val navigateToSingleRecipeFragment = SingleLiveEvent<Long>()
    val navigateToRecipeContentFragment = SingleLiveEvent<Long>()
    private val currentRecipe = MutableLiveData<Recipe?>(null)

    fun onAddClick() {
        navigateToRecipeContentFragment.call()
    }

//    fun onSaveButtonClick(content: String) {
//        if (content.isBlank()) return
//        val recipe = currentRecipe.value?.copy(
//            content = content) ?: Recipe(
//            recipeId = Repository.NEW_RECIPE_ID,
//            recipeName = "Первый",//TODO заполняемое при создании
//            author = "me",
//            category = Category.AMERICAN,//TODO заполняемое при создании
//            content = content,
//        )
//        repository.save(recipe)
//        currentRecipe.value = null
//    }

    override fun onEditClick(recipe: Recipe) {
        currentRecipe.value = recipe
        navigateToRecipeContentFragment.value = recipe.recipeId
    }

    override fun onRemoveClick(recipeId: Long) = repository.delete(recipeId)

    override fun onContentClick(recipe: Recipe) {
        navigateToSingleRecipeFragment.value = recipe.recipeId
    }

    override fun onAddToFavoritesClick(recipeId: Long) = repository.addToFavorites(recipeId)


}