package ru.netology.nmedia.adapter

import ru.netology.recipiesbook.Main.data.Recipe


interface RecipeInteractionListener {
    fun onRemoveClick(recipeId : Long)
    fun onEditClick(recipe : Recipe)
    fun onContentClick(recipe : Recipe)
    fun onAddToFavoritesClick (recipeId: Long)
}