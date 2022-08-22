package ru.netology.recipiesbook.Main.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.netology.recipiesbook.Main.db.RecipeEntity

// ImageSourse - Общая картинка рецепта, картинка каждого шага лежит в RecipeContent
// Поля content и url
@Serializable
data class Recipe(
    val recipeId: Long,
    val recipeName: String,
    val author: String = "Netology",
    val category: Category? = null,
    val content: MutableList<RecipeContent>? = null,
    val mainImageSource: String? = null,
    var addedToFavorites: Boolean = false,
){
    //TODO сделать запись контента
    fun toEntity() = RecipeEntity(
        recipeId = recipeId,
        recipeName = recipeName,
        author = author,
        category = category.toString(),
        content = Json.encodeToString(content),
        imageSource = mainImageSource,
        addedToFavorites = addedToFavorites,
    )
}
enum class Category {
    EUROPEAN, ASIAN, PANASIAN, EASTERN, AMERICAN, RUSSIAN, MEDITERRANEAN
}
