package ru.netology.recipiesbook.Main.data

import androidx.lifecycle.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.netology.recipiesbook.Main.data.Repository.Companion.NEW_RECIPE_ID
import ru.netology.recipiesbook.Main.db.RecipeDao

class MainRepository(private val dao: RecipeDao) : Repository {

    override val data = dao.getAll().map { entities ->
        entities.map { it.toModel() }
    }

    override fun delete(recipeId: Long) {
        dao.removeById(recipeId)
    }

    override fun addToFavorites(recipeid: Long) {
        dao.addToFavorites(recipeid)
    }

    override fun save(recipe: Recipe) {
        if (recipe.recipeId == NEW_RECIPE_ID) dao.insert(recipe.toEntity())
        else dao.updateContentById(
            recipe.recipeId,
            recipe.recipeName,
            recipe.author,
            recipe.category.toString(),
            Json.encodeToString(recipe.content),
            recipe.mainImageSource
        )
    }

}