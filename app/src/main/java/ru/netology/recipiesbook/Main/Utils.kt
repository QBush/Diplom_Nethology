package ru.netology.recipiesbook.Main

import ru.netology.recipiesbook.Main.data.Recipe
import ru.netology.recipiesbook.Main.data.RecipeContent


fun findRecipeById(recipeId: Long, recipes: List<Recipe>) = recipes.firstOrNull() {
    it.recipeId == recipeId
}

fun updateRecipeStepsNumbers(recipe: Recipe?) =
    recipe?.content?.forEachIndexed { index, recipeContent ->
        recipeContent.stepNumber = index
    }