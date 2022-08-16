package ru.netology.recipiesbook.Main.data

import androidx.lifecycle.LiveData

interface Repository {

    val data: LiveData<List<Recipe>>

    fun delete(recipeId: Long)
    fun save(recipe: Recipe)
    fun addToFavorites(recipeId: Long)


    companion object {
        const val NEW_RECIPE_ID = 0L

    }
}