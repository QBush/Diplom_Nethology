package ru.netology.recipiesbook.Main.data

import kotlinx.serialization.Serializable
import ru.netology.recipiesbook.Main.db.RecipeEntity

// ImageSourse - Общая картинка рецепта, картинка каждого шага лежит в RecipeContent
// Поля content и url
@Serializable
data class Recipe(
    val recipeId: Long = 0L,
    val recipeName: String,
    val author: String = "Netology",
    val category: Category,
    val content: MutableList<RecipeContent>,
    val mainImageSource: String? = "R.id.baseRecipeImage",
    var addedToFavorites: Boolean = false,
){
    //TODO сделать запись контента
    fun toEntity() = RecipeEntity(
        recipeId = recipeId,
        recipeName = recipeName,
        author = author,
        category = category,
        content = content,
        imageSource = mainImageSource,
        addedToFavorites = addedToFavorites,
    )
}
enum class Category {
    EUROPEAN, ASIAN, PANASIAN, EASTERN, AMERICAN, RUSSIAN, MEDITERRANEAN
}
