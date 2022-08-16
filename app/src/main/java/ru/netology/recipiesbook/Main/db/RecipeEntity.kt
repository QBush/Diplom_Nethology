package ru.netology.recipiesbook.Main.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.recipiesbook.Main.data.Category
import ru.netology.recipiesbook.Main.data.Recipe
import ru.netology.recipiesbook.Main.data.RecipeContent

@Entity(tableName = "recipes")
class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "recipeId")
    val recipeId: Long,
    @ColumnInfo(name = "recipeName")
    val recipeName: String,
    @ColumnInfo(name = "author")
    val author: String,
    @ColumnInfo(name = "Category")
    val category: Category,
    @ColumnInfo(name = "content")
    val content: List<RecipeContent>,
    @ColumnInfo(name = "ImageSource")
    var imageSource: String?,
    @ColumnInfo(name = "addedToFavorites")
    var addedToFavorites: Boolean = false
){
    // TODO сделать извлечение контента
    fun toModel() = Recipe(
        recipeId = recipeId,
        recipeName = recipeName,
        author = author,
        category = category,
        content = content,
        mainImageSource = imageSource,
        addedToFavorites = addedToFavorites,
    )
}