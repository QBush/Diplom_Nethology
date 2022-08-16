package ru.netology.recipiesbook.Main.data

import kotlinx.serialization.Serializable

@Serializable
data class RecipeContent(
    //TODO нужен метод синхронизации номера шага и номера в массиве или ...
    val stepNumber: Int = 0,
    val stepContent: String,
    val stepImageURL: String? = null
) {

}

