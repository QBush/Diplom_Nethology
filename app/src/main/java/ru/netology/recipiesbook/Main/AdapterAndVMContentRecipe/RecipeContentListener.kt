package ru.netology.recipiesbook.Main.AdapterAndVMContentRecipe

import ru.netology.recipiesbook.Main.data.Recipe

interface RecipeContentListener {
    fun onDeleteStepClick(stepNumber: Int)
    fun onSaveButtonClick(recipe: Recipe)
}