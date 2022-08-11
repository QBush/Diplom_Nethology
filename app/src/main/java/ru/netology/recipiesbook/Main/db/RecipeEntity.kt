package ru.netology.recipiesbook.Main.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.recipiesbook.Main.data.Recipe

@Entity(tableName = "recipes")
class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "recipeId")
    val recipeId: Long,
    @ColumnInfo(name = "author")
    val author: String,
    @ColumnInfo(name = "content")
    val content: String,
    @ColumnInfo(name = "publishedDate")
    val published: String,
    @ColumnInfo(name = "ImageSource")
    var ImageSource: String?,
    @ColumnInfo(name = "addedToFavorites")
    var addedToFavorites: Boolean = false
){

    fun toModel() = Recipe(
        recipeId = recipeId,
        author = author,
        content = content,
        published = published,
        ImageSource = ImageSource,
        addedToFavorites = addedToFavorites,
    )

    fun toEntity() = Recipe(
        recipeId = recipeId,
        author = author,
        content = content,
        published = published,
        ImageSource = ImageSource,
        addedToFavorites = addedToFavorites,
    )

}