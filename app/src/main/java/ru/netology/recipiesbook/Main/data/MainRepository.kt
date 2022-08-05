package ru.netology.recipiesbook.Main.data

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainRepository(private val application : Application): Repository {

    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Recipe::class.java).type

    private var recipes // значение data.value, проверенное на null
        get() = checkNotNull(data.value) {
            "value should not be null"
        }
        set(value) { // запись  в поток при каждом обновлении списка
            application.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).bufferedWriter().use {
                it.write(gson.toJson(value))
            }
            data.value = value
        }

    override val data: MutableLiveData<List<Recipe>>

    init { // читаем с потока при старте
        val recipeListFile = application.filesDir.resolve(FILE_NAME)
        val posts: List<Recipe> = if (recipeListFile.exists()) {
            val inputStream = application.openFileInput(FILE_NAME)
            val reader = inputStream.bufferedReader()
            reader.use {
                gson.fromJson(it, type)
            }
        } else emptyList()
        data = MutableLiveData(posts)
    }

    private companion object {
        const val FILE_NAME = "recipesList.json"
    }

    override fun addNew(recipeId: Long) {
        TODO("Not yet implemented")
    }

    override fun share(recipeId: Long) {
        TODO("Not yet implemented")
    }

    override fun delete(recipeId: Long) {
        TODO("Not yet implemented")
    }

    override fun save(recipe: Recipe) {
        TODO("Not yet implemented")
    }

    override fun addToFavorites(recipe: Recipe) {
        TODO("Not yet implemented")
    }

}