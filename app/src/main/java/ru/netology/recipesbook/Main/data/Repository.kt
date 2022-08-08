package ru.netology.recipesbook.Main.data

import androidx.lifecycle.LiveData

interface Repository {

    val data: LiveData<List<Recipe>>

    fun share(recipeId: Long)
    fun delete(recipeId: Long)
    fun save(recipe: Recipe)
    fun addToFavorites(recipe: Recipe)


    companion object {
        const val NEW_RECIPE_ID = 0L
    }
}