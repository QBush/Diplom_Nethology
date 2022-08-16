package ru.netology.recipiesbook.Main

/ / Активити для редактирования и создания поста

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.netology.recipiesbook.Main.AdapterAndVMContentRecipe.RecipeContentAdapter
import ru.netology.recipiesbook.Main.AdapterAndVMContentRecipe.RecipeContentViewModel
import ru.netology.recipiesbook.Main.data.Category
import ru.netology.recipiesbook.Main.data.Recipe
import ru.netology.recipiesbook.Main.data.RecipeContent

import ru.netology.recipiesbook.databinding.RecipeContentFragmentBinding

//TODO реализовать сохранение предыдущих шагов при создании нового рецепта
//TODO как-то умненьшить количество вопросительных знаков
class RecipeContentFragment : Fragment() {

    private val viewModel by viewModels<RecipeContentViewModel>(ownerProducer = ::requireParentFragment)
    private val args by navArgs<RecipeContentFragmentArgs>()

    private val previousContent = context?.getSharedPreferences(
        "previousNewContent", Context.MODE_PRIVATE
    )
    private val currentId = args.recipeId

    val content = previousContent?.getString(SAVED_RECIPE_KEY, null)

    private val previousRecipeContent: Recipe? = if (content != null) {
        Json.decodeFromString(content)
    } else null

    private var currentRecipe = findRecipeById(currentId, viewModel.data.value!!)
        ?: previousRecipeContent

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = RecipeContentFragmentBinding.inflate(layoutInflater, container, false)
        .also { binding ->

            val adapter = RecipeContentAdapter(viewModel)
            binding.recipeStepsContentFragment.adapter = adapter

            with(binding) {
                recipeName.setText(currentRecipe?.recipeName ?: FREE_SPACE)
                //TODO сделать выбор из ENUM ниже
                category.setText(currentRecipe?.category.toString())
                mainRecipeImage.setText(currentRecipe?.mainImageSource ?: FREE_SPACE)
// добавляет шаг рецепта, обновляет индексы и адаптер
                plusStepButton.setOnClickListener {
                    currentRecipe?.content?.add(
                        RecipeContent(
                            stepContent = FREE_SPACE
                        )
                    )
                    updateRecipeStepsNumbers(currentRecipe)
                    adapter.submitList(currentRecipe?.content)
                }
            }

            viewModel.deleteStepEvent.observe(viewLifecycleOwner) {
                currentRecipe?.content?.removeAll() { recipeStep ->
                    recipeStep.stepNumber == it
                }
                updateRecipeStepsNumbers(currentRecipe)
                adapter.submitList(currentRecipe?.content)
            }

            requireActivity().onBackPressedDispatcher.addCallback(this) {
                updateCurrentRecipe(binding)
                previousContent?.edit {
                    Json.encodeToString(currentRecipe)
                    putString(SAVED_RECIPE_KEY, currentRecipe.toString())
                }
                findNavController().popBackStack()
            }

//TODO передавать массив по кнопке ok
            binding.ok.setOnClickListener {
                updateCurrentRecipe(binding)
                if (
                    binding.recipeName.text.isBlank() ||
                    binding.category.text.isBlank() ||
                    currentRecipe?.content.isNullOrEmpty()
                ) {
                    //TODO вывести сообщение "заполните все поля"
                }

                try {
                    viewModel.onSaveButtonClick(currentRecipe!!)
                } catch (e: NullPointerException) {
                    println("Текущий рецепт имеет нулевое значение")
                }
                previousContent?.edit()?.clear()?.apply()
                findNavController().popBackStack()
            }
        }.root

    companion object {
        const val REQUEST_KEY = "RequestKey"
        const val CONTENT_KEY = "PostContent"
        const val URL_KEY = "PostUrl"

        private const val SAVED_RECIPE_KEY = "PreviousNewRecipeContent"
        private const val FREE_SPACE = ""
    }

    //TODO как установить ENUM значение
    fun updateCurrentRecipe(binding: RecipeContentFragmentBinding) {
        val currentName = binding.recipeName.text.toString()
        val mainImageSource = binding.mainRecipeImage.text.toString()
        val currentCategory = binding.category.text.toString()
        val emptyStep = mutableListOf(
            RecipeContent(
                stepContent = FREE_SPACE
            )
        )
        currentRecipe =
            Recipe(
                recipeId = currentId,
                recipeName = currentName,
                mainImageSource = mainImageSource,
                category = Category.currentCategory,
                content = currentRecipe?.content ?: emptyStep
            )
    }
}
