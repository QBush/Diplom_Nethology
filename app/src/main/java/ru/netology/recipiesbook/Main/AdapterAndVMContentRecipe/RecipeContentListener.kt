package ru.netology.recipiesbook.Main.AdapterAndVMContentRecipe

import ru.netology.recipiesbook.Main.data.Recipe
import ru.netology.recipiesbook.Main.data.RecipeContent

interface RecipeContentListener {
    fun onDeleteStepClick(stepNumber: Int)
    fun onSaveButtonClick(recipe: Recipe)
    fun onSaveStepClick(recipeContent: RecipeContent)
}