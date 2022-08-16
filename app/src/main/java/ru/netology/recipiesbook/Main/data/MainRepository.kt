package ru.netology.recipiesbook.Main.data

import android.app.Application
import android.content.Context
import android.provider.SyncStateContract.Helpers.insert
import android.provider.SyncStateContract.Helpers.update
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.recipiesbook.Main.data.Repository.Companion.NEW_RECIPE_ID
import ru.netology.recipiesbook.Main.db.RecipeDao

class MainRepository(private val dao: RecipeDao): Repository {

//TODO сделать метод save Массив в стринг переделать
    override val data = dao.getAll().map{ entities ->
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
        else dao.updateContentById(recipe.recipeId, recipe.toEntity())
    }


}