package ru.netology.recipiesbook.Main.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable
import ru.netology.recipiesbook.Main.db.RecipeEntity

// ImageSourse - Общая картинка рецепта, картинка каждого шага лежит в RecipeContent
@Serializable
@Parcelize
data class Recipe(
    val recipeId: Long,
    val recipeName: String,
    val author: String,
    val category: Category,
    val content: List<RecipeContent>,
    val imageSource: String? = "R.id.baseRecipeImage",
    var addedToFavorites: Boolean = false,
) : Parcelable {
    fun toEntity() = RecipeEntity(
        recipeId = recipeId,
        recipeName = recipeName,
        author = author,
        category = category,
        content = content,
        imageSource = imageSource,
        addedToFavorites = addedToFavorites,
    )
}
enum class Category {
    EUROPEAN, ASIAN, PANASIAN, EASTERN, AMERICAN, RUSSIAN, MEDITERRANEAN
}
