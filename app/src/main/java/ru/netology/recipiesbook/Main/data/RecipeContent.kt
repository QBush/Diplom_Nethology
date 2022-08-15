package ru.netology.recipiesbook.Main.data

import kotlinx.serialization.Serializable

@Serializable
data class RecipeContent(
    val stepNumber: Int,
    val stepContent: String,
    val stepImageURL: String? = null
) {

}

