package ru.netology.recipiesbook.Main

import ru.netology.recipiesbook.Main.data.Recipe


fun findRecipeById(recipeId: Long, recipes: List<Recipe>) = recipes.firstOrNull() {
    it.recipeId == recipeId
}