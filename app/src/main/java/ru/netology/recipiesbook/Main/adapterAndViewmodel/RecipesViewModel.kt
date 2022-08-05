package ru.netology.recipiesbook.Main.adapterAndViewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.netology.nmedia.adapter.RecipeInteractionListener
import ru.netology.recipiesbook.Main.data.MainRepository
import ru.netology.recipiesbook.Main.data.Recipe
import ru.netology.recipiesbook.Main.data.Repository

class RecipesViewModel(
    application: Application
) : AndroidViewModel(application), RecipeInteractionListener {

    private val repository: Repository = MainRepository(application)

    override fun onAddClick(recipe: Recipe) {
        TODO("Not yet implemented")
    }

    override fun onRemoveClick(recipe: Recipe) {
        TODO("Not yet implemented")
    }

    override fun onEditClick(recipe: Recipe) {
        TODO("Not yet implemented")
    }

    override fun onContentClick(recipe: Recipe) {
        TODO("Not yet implemented")
    }

}