package ru.netology.recipiesbook.Main.data

import kotlinx.serialization.Serializable

@Serializable
data class RecipeContent(
    var stepNumber: Int = 0,
    var stepContent: String = "",
    var stepImageURL: String? = null,
    var saved: Boolean = false
) {

}

