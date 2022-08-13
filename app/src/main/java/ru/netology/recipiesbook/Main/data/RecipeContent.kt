package ru.netology.recipiesbook.Main.data

import kotlinx.serialization.Serializable

@Serializable
class RecipeContent(
    val stepContent: String,
    val stepImageURL: String? = null
) {



}