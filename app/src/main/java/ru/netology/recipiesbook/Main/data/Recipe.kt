package ru.netology.recipiesbook.Main.data

import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    val recipeId: Long,
    val author: String,
    val content: String,
    val ImageSource: String,
    var addedToFavorites: Boolean = false,
){


}
