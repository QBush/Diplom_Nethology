package ru.netology.recipiesbook.Main

// Фрагмент для редактирования и создания поста

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
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
import ru.netology.recipiesbook.R
import ru.netology.recipiesbook.databinding.RecipeContentFragmentBinding
import java.util.Collections.addAll

//Фрагмент для редактирования и добавления рецептов. Добавление картинок происходит через указание ссылок
//TODO не отображается удаление, утверждение(сохранение) и добавление шагов во всех ситуациях
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

            val adapter = RecipeContentAdapter(viewModel)
            binding.recipeStepsContentFragment.adapter = adapter
//получаем сохраненный преференс
            val previousContent = context?.getSharedPreferences(
                "previousNewContent", Context.MODE_PRIVATE
            )
// читаем преференс
            val content = previousContent?.getString(SAVED_RECIPE_KEY, null)

// декодируем преференс (сохраняется по кнопке "назад")
            val previousRecipeContent: Recipe? = if (content != null) {
                Json.decodeFromString(content)
            } else null


//назначаем текущий рецепт либо из репозитория, либо если там null, то берем сохраненный ранее
            viewModel.data.observe(viewLifecycleOwner) { recipe ->
                var currentRecipe = findRecipeById(currentId, recipe)
                    ?: previousRecipeContent


                with(binding) {
// если был редактируемый рецепт или был сохраненный по кнопке назад, то устанавливаем эти значения
                    if (currentRecipe != null) {
                        recipeName.setText(currentRecipe?.recipeName ?: FREE_SPACE)
                        if (currentRecipe?.category != null) {
                            category.setText(currentRecipe?.category.toString())
                        }
                        mainRecipeImage.setText(currentRecipe?.mainImageSource ?: FREE_SPACE)
                        viewModel.stepList.value = currentRecipe?.content?.toMutableList()
                    }

//убираем отображение клавиатуры
                    category.showSoftInputOnFocus = false
                    mainRecipeImage.showSoftInputOnFocus = false

//обработка добавления шага
                    plusStepButton.setOnClickListener {
                        if (
                            binding.recipeName.text.isBlank() ||
                            binding.category.text.isBlank()
                        ) {
                            Toast.makeText(context, R.string.fill_fields, Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }
                        viewModel.stepList.value = (viewModel.stepList.value.orEmpty() +
                                RecipeContent(
                                    stepContent = FREE_SPACE
                                )).toMutableList()
                    }
                }

// Конструкция для выпадающего списка
                val categories = resources.getStringArray(R.array.categories_array)
                val categoryAdapter = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_list_item_1,
                        categories
                    )
                }
                with(binding) {
                    category.setAdapter(categoryAdapter)
                    category.onItemClickListener =
                        AdapterView.OnItemClickListener { adapterView, _, position, _ ->
                            val selectedItem = adapterView.getItemAtPosition(position).toString()
                            Toast.makeText(context, "Selected: $selectedItem", Toast.LENGTH_SHORT)
                                .show()
                        }
                    category.setOnDismissListener {
                        Toast.makeText(context, "_", Toast.LENGTH_SHORT).show()
                    }
                    category.onFocusChangeListener = View.OnFocusChangeListener { it, hasFocus ->
                        if (hasFocus) {
                            category.showDropDown()
                        }
                    }
                }

//TODO шаги все равно не обновляются адаптером
                viewModel.stepList.observe(viewLifecycleOwner) {
                    updateRecipeStepsNumbers(viewModel.stepList.value)
                    currentRecipe =
                        updateCurrentRecipe(
                            binding,
                            currentRecipe,
                            viewModel.stepList.value,
                            currentId
                        )
                    adapter.submitList(it)
                }

// при движении назад сохраняем Преф
                requireActivity().onBackPressedDispatcher.addCallback(this) {
                    currentRecipe = updateCurrentRecipe(
                        binding,
                        currentRecipe,
                        viewModel.stepList.value,
                        currentId
                    )
                    previousContent?.edit {
                        putString(SAVED_RECIPE_KEY, Json.encodeToString(currentRecipe))
                    }
                    viewModel.stepList.value?.clear()
                    findNavController().popBackStack()
                }

// сохранение рецепта
                binding.ok.setOnClickListener {
                    if (
                        binding.recipeName.text.isBlank() ||
                        binding.category.text.isBlank()
                    ) {
                        Toast.makeText(context, R.string.fill_fields, Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    if (
                        currentRecipe?.content.isNullOrEmpty()
                    ) {
                        Toast.makeText(context, R.string.step_needed, Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    updateRecipeStepsNumbers(viewModel.stepList.value)
                    currentRecipe =
                        updateCurrentRecipe(
                            binding,
                            currentRecipe,
                            viewModel.stepList.value,
                            currentId
                        )
                    try {
                        viewModel.onSaveButtonClick(currentRecipe!!)
                    } catch (e: NullPointerException) {
                        println("Текущий рецепт имеет нулевое значение")
                    }
                    previousContent?.edit()?.clear()?.apply()
                    findNavController().popBackStack()
                }
            }
        }.root


    companion object {
        private const val SAVED_RECIPE_KEY = "PreviousNewRecipeId"
        private const val FREE_SPACE = ""
    }


    private fun updateCurrentRecipe(
        binding: RecipeContentFragmentBinding,
        currentRecipe: Recipe?,
        currentStepList: MutableList<RecipeContent>?,
        currentId: Long
    ): Recipe? {
        val currentCategory = binding.category.text.toString()
        //если поле категории пустое
        if (!Category.values().map { it.name }.contains(currentCategory)) {
            return currentRecipe?.copy(
                recipeId = currentId,
                recipeName = binding.recipeName.text.toString(),
                mainImageSource = binding.mainRecipeImage.text.toString(),
                content = currentStepList
            )
        } else if (currentRecipe != null) {
            return currentRecipe.copy(
                recipeId = currentId,
                recipeName = binding.recipeName.text.toString(),
                mainImageSource = binding.mainRecipeImage.text.toString(),
                category = Category.valueOf(binding.category.text.toString()),
                content = currentStepList
            )
        }
        return Recipe(
            recipeId = currentId,
            recipeName = binding.recipeName.text.toString(),
            mainImageSource = binding.mainRecipeImage.text.toString(),
            category = Category.valueOf(binding.category.text.toString()),
            content = currentStepList
        )
    }
}



