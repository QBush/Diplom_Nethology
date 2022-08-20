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
import ru.netology.recipiesbook.Main.utils.BottomBarHideInterface
import ru.netology.recipiesbook.R

import ru.netology.recipiesbook.databinding.RecipeContentFragmentBinding

//TODO убрать BottomBar - нужно получить доступ к активити
//TODO не работает добавление шагов 2-го и последующих
//TODO не видно кнопки Save (см первый пункт исправлений)
//TODO по кнопке "назад" не сохраняются шаги
//TODO выводить сообщения о пустых полях
class RecipeContentFragment : Fragment(), BottomBarHideInterface {

    private val viewModel by viewModels<RecipeContentViewModel>(ownerProducer = ::requireParentFragment)
    private val args by navArgs<RecipeContentFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = RecipeContentFragmentBinding.inflate(layoutInflater, container, false)
        .also { binding ->

            (requireActivity() as AppActivity).hideBottomBar(true)

            // берем переданный ID
            val currentId = args.recipeId

            //получаем сохраненный преференс
            val previousContent = context?.getSharedPreferences(
                "previousNewContent", Context.MODE_PRIVATE
            )

            // читаем преференс
            val content = previousContent?.getString(SAVED_RECIPE_KEY, null)
            val previousName = previousContent?.getString(SAVED_JUST_NAME_KEY, null)

            // декодируем преференс
            val previousRecipeContent: Recipe? = if (content != null) {
                Json.decodeFromString(content)
            } else null

//TODO из edit не то приходит, currentRecipe = null почему то
            //назначаем текущий рецепт либо старым, либо если там null, то берем сохраненный ранее
            var currentRecipe = viewModel.data.value?.let { findRecipeById(currentId, it) }
                ?: previousRecipeContent

            val adapter = RecipeContentAdapter(viewModel)
            binding.recipeStepsContentFragment.adapter = adapter


            with(binding) {
                recipeName.setText(currentRecipe?.recipeName ?: previousName)
                if (currentRecipe?.category != null) {
                    category.setText(currentRecipe?.category.toString())
                }
                //убираем отображение клавиатуры
                category.showSoftInputOnFocus = false
                mainRecipeImage.showSoftInputOnFocus = false

                mainRecipeImage.setText(currentRecipe?.mainImageSource ?: FREE_SPACE)
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
                    currentRecipe = updateCurrentRecipe(binding, currentRecipe, currentId)
                    currentRecipe?.content?.add(
                        RecipeContent(
                            stepContent = FREE_SPACE
                        )
                    )
                    updateRecipeStepsNumbers(currentRecipe)
                    adapter.submitList(currentRecipe?.content)
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
                adapter.submitList(currentRecipe?.content)
            }

            // при движении назад сохраняем Преф
            requireActivity().onBackPressedDispatcher.addCallback(this) {
                if (!binding.category.text.isNullOrEmpty()) {
                    currentRecipe = updateCurrentRecipe(binding, currentRecipe, currentId)
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
    ): Recipe = Recipe(
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
