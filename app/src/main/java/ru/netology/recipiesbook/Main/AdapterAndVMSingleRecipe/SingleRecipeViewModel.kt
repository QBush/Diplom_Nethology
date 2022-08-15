package ru.netology.recipiesbook.Main.AdapterAndVMSingleRecipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.netology.nmedia.adapter.RecipeInteractionListener
import ru.netology.nmedia.db.AppDb
import ru.netology.recipiesbook.Main.data.MainRepository
import ru.netology.recipiesbook.Main.data.Recipe
import ru.netology.recipiesbook.Main.data.Repository

class SingleRecipeViewModel(
    application: Application
): AndroidViewModel(application){

    private val repository: Repository = MainRepository(
        dao = AppDb.getInstance(context = application).postDao
    )
    val data by repository::data


}