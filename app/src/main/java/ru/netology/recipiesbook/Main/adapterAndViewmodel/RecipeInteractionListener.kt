package ru.netology.nmedia.adapter

import ru.netology.recipiesbook.Main.data.Recipe


interface RecipeInteractionListener {
    fun onRemoveClick(recipe : Recipe)
    fun onEditClick(recipe : Recipe)
    fun onContentClick(recipe : Recipe)
}