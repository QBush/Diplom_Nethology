package ru.netology.recipiesbook.Main.data

import kotlinx.serialization.Serializable

@Serializable
data class RecipeContent(
    var stepNumber: Int = 0,
    val stepContent: String = "",
    val stepImageURL: String? = null
) {

}

