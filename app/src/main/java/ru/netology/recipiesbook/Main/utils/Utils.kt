package ru.netology.recipiesbook.Main

import ru.netology.recipiesbook.Main.data.Recipe
import ru.netology.recipiesbook.Main.data.RecipeContent


fun findRecipeById(recipeId: Long, recipes: List<Recipe>?) =
    if (recipes.isNullOrEmpty()) null else recipes.firstOrNull { it.recipeId == recipeId }

//для обновления номеров шагов в случае удаления шага
fun updateRecipeStepsNumbers(recipeList: MutableList<RecipeContent>?) =
    recipeList?.forEachIndexed { index, recipeContent ->
        recipeContent.stepNumber = index
//        recipeList.sortBy {recipeContent.stepNumber}
    }




