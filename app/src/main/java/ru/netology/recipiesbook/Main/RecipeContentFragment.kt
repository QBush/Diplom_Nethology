package ru.netology.recipiesbook.Main

// Фрагмент для редактирования и создания поста

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
import kotlinx.serialization.json.decodeFromJsonElement
import ru.netology.recipiesbook.Main.AdapterAndVMContentRecipe.RecipeContentAdapter
import ru.netology.recipiesbook.Main.AdapterAndVMContentRecipe.RecipeContentViewModel
import ru.netology.recipiesbook.Main.data.Category
import ru.netology.recipiesbook.Main.data.Recipe
import ru.netology.recipiesbook.Main.data.RecipeContent
import ru.netology.recipiesbook.Main.data.Repository.Companion.NEW_RECIPE_ID

import ru.netology.recipiesbook.databinding.RecipeContentFragmentBinding

//TODO как-то умненьшить количество вопросительных знаков. get() checkNotNull
//TODO убрать BottomBar
//TODO убрать null из категории при старте
//TODO не работает добавление шагов
class RecipeContentFragment : Fragment() {

    private val viewModel by viewModels<RecipeContentViewModel>(ownerProducer = ::requireParentFragment)
    private val args by navArgs<RecipeContentFragmentArgs>()




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = RecipeContentFragmentBinding.inflate(layoutInflater, container, false)
        .also { binding ->

            // берем переданный ID
            val currentId = args.recipeId

            //получаем сохраненный преференс
            val previousContent = context?.getSharedPreferences(
                "previousNewContent", Context.MODE_PRIVATE
            )

            // читаем преференс
            val content = previousContent?.getString(SAVED_RECIPE_KEY, null)

            // декодируем преференс
            val previousRecipeContent: Recipe? = if (content != null) {
                Json.decodeFromString(content)
            } else null

            //назначаем текущий рецепт либо старым, либо если там null, то берем сохраненный ранее
            var currentRecipe = viewModel.data.value?.let { findRecipeById(currentId, it) }
                ?: previousRecipeContent

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

            // при движении назад сохраняем Преф
            requireActivity().onBackPressedDispatcher.addCallback(this) {
                currentRecipe = updateCurrentRecipe(binding, currentRecipe, currentId)
                previousContent?.edit {
                    putString(SAVED_RECIPE_KEY, Json.encodeToString(currentRecipe))
                }
                findNavController().popBackStack()
            }

            binding.ok.setOnClickListener {
                currentRecipe = updateCurrentRecipe(binding, currentRecipe, currentId)
                if (
                    binding.recipeName.text.isBlank() ||
                    binding.category.text.isBlank()
                ) {
                    //TODO вывести сообщение "заполните все поля"
                    return@setOnClickListener
                }
                if (
                    currentRecipe?.content.isNullOrEmpty()
                ) {
                    //TODO вывести сообщение "рецепт должен содержать как минимум 1 этап"
                    return@setOnClickListener
                }

                try {
                    viewModel.onSaveButtonClick(currentRecipe!!)
                } catch (e: NullPointerException) {
                    println("Текущий рецепт имеет нулевое значение")
                }
                previousContent?.edit()?.clear()?.apply()
                findNavController().popBackStack()
            }
            //TODO
            binding.plusStepButton.setOnClickListener {
                currentRecipe?.content?.add(RecipeContent())
            }

        }.root


    companion object {
        private const val SAVED_RECIPE_KEY = "PreviousNewRecipeId"
        private const val FREE_SPACE = ""
    }

    //TODO как установить ENUM значение
    private fun updateCurrentRecipe(
        binding: RecipeContentFragmentBinding,
        currentRecipe: Recipe?,
        currentId: Long): Recipe = Recipe(
                recipeId = currentId,
                recipeName = binding.recipeName.text.toString(),
                mainImageSource = binding.mainRecipeImage.text.toString(),
                category = Category.valueOf(binding.category.text.toString()),
                content = currentRecipe?.content ?: mutableListOf(
                    RecipeContent(
                        stepContent = FREE_SPACE
                    )
                )
            )

}
