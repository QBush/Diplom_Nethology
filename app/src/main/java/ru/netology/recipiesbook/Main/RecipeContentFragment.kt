package ru.netology.recipiesbook.Main

// Фрагмент для редактирования и создания поста

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.view.WindowManager
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
import kotlinx.serialization.json.decodeFromJsonElement
import ru.netology.recipiesbook.Main.AdapterAndVMContentRecipe.RecipeContentAdapter
import ru.netology.recipiesbook.Main.AdapterAndVMContentRecipe.RecipeContentViewModel
import ru.netology.recipiesbook.Main.data.Category
import ru.netology.recipiesbook.Main.data.Recipe
import ru.netology.recipiesbook.Main.data.RecipeContent
import ru.netology.recipiesbook.Main.data.Repository.Companion.NEW_RECIPE_ID
import ru.netology.recipiesbook.R
import ru.netology.recipiesbook.databinding.RecipeContentFragmentBinding

//TODO не работает добавление шагов 2-го и последующих
//TODO по кнопке "назад" не сохраняются шаги
//TODO выводить сообщения о пустых полях
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
            val previousName = previousContent?.getString(SAVED_JUST_NAME_KEY, null)

            // декодируем преференс (сохраняется по кнопке "назад")
            val previousRecipeContent: Recipe? = if (content != null) {
                Json.decodeFromString(content)
            } else null

            val currentRecipeList = viewModel.data.value
            val currentStepList = mutableListOf<RecipeContent>()
//TODO из edit не то приходит, currentRecipe = null почему то
//назначаем текущий рецепт либо старым, либо если там null, то берем сохраненный ранее
            var currentRecipe = findRecipeById(currentId, currentRecipeList)
                ?: previousRecipeContent

            val adapter = RecipeContentAdapter(viewModel)
            binding.recipeStepsContentFragment.adapter = adapter


            with(binding) {
// если был редактируемый рецепт или был сохраненный по кнопке назад, то устанавливаем эти значения
                if (currentRecipe != null) {
                    recipeName.setText(currentRecipe?.recipeName ?: previousName)
                    if (currentRecipe?.category != null) {
                        category.setText(currentRecipe?.category.toString())
                    }
                    mainRecipeImage.setText(currentRecipe?.mainImageSource ?: FREE_SPACE)
                } else {
                    recipeName.setText(previousName)
                }
                //убираем отображение клавиатуры
                category.showSoftInputOnFocus = false
                mainRecipeImage.showSoftInputOnFocus = false


// TODO не работает добавление 2-го и последующего шагов, не обновляет адаптер
                //обработка добавления
                plusStepButton.setOnClickListener {
                    if (
                        binding.recipeName.text.isBlank() ||
                        binding.category.text.isBlank()
                    ) {
                        Toast.makeText(context, R.string.fill_fields, Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    // TODO currentRecipe null почему-то
                    currentRecipe = updateCurrentRecipe(binding, currentRecipe, currentId)
                    currentRecipe?.content?.add(
                        RecipeContent(
                            stepContent = FREE_SPACE
                        )
                    )
                    updateRecipeStepsNumbers(currentRecipe)
                    val currentStepList = mutableListOf<RecipeContent>()
                    currentRecipe?.content?.let { it1 -> currentStepList.addAll(it1) }
                    adapter.submitList(currentStepList)
                }
            }

            // выпадающий текст
            val countries = resources.getStringArray(R.array.countries_array)
            val categoryAdapter = context?.let {
                ArrayAdapter(
                    it,
                    android.R.layout.simple_list_item_1,
                    countries
                )
            }
            //TODO если сбросить фокус один раз, то выбор не вылезает повторно на поле категория
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
//TODO не работает удаление, не обновляет адаптер
            viewModel.deleteStepEvent.observe(viewLifecycleOwner) {
                currentRecipe?.content?.removeAll() { recipeStep ->
                    recipeStep.stepNumber == it
                }
                updateRecipeStepsNumbers(currentRecipe)
                val currentStepList = currentRecipe?.content
                adapter.submitList(currentStepList)
            }

            // при движении назад сохраняем Преф
            requireActivity().onBackPressedDispatcher.addCallback(this) {
                if (!binding.category.text.isNullOrEmpty()) {
                    currentRecipe = updateCurrentRecipe(binding, currentRecipe, currentId)
                    // TODO добавить в currentRecipe шаги
                    previousContent?.edit {
                        putString(SAVED_RECIPE_KEY, Json.encodeToString(currentRecipe))
                    }
                } else {
                    previousContent?.edit {
                        putString(SAVED_JUST_NAME_KEY, binding.recipeName.text.toString())
                    }
                }
                findNavController().popBackStack()
            }


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

                currentRecipe = updateCurrentRecipe(binding, currentRecipe, currentId)
                // TODO добавить в currentRecipe шаги
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
        private const val SAVED_RECIPE_KEY = "PreviousNewRecipeId"
        private const val SAVED_JUST_NAME_KEY = "PreviousNewName"
        private const val FREE_SPACE = ""
    }


    private fun updateCurrentRecipe(
        binding: RecipeContentFragmentBinding,
        currentRecipe: Recipe?,
        currentId: Long
    ) = if (currentRecipe != null) {
        currentRecipe.copy(
            recipeId = currentId,
            recipeName = binding.recipeName.text.toString(),
            mainImageSource = binding.mainRecipeImage.text.toString(),
            category = Category.valueOf(binding.category.text.toString()),

        )
    } else {
        Recipe (
            recipeId = currentId,
            recipeName = binding.recipeName.text.toString(),
            mainImageSource = binding.mainRecipeImage.text.toString(),
            category = Category.valueOf(binding.category.text.toString()),
                )
    }
}
