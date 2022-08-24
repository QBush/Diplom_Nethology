package ru.netology.recipiesbook.Main

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import ru.netology.recipiesbook.Main.data.Recipe
import ru.netology.recipiesbook.Main.data.RecipeContent


fun findRecipeById(recipeId: Long, recipes: List<Recipe>?)
= if (recipes.isNullOrEmpty()) null else recipes.firstOrNull { it.recipeId == recipeId}


fun updateRecipeStepsNumbers(recipeList: MutableList<RecipeContent>?) =
    recipeList?.forEachIndexed { index, recipeContent ->
        recipeContent.stepNumber = index
    }


