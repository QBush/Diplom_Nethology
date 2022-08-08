package ru.netology.nmedia.adapter

import ru.netology.recipesbook.Main.data.Recipe


interface RecipeInteractionListener {
    fun onRemoveClick(recipe : Recipe)
    fun onEditClick(recipe : Recipe)
    fun onContentClick(recipe : Recipe)
}