package ru.netology.recipiesbook.Main
// Активити для редактирования и создания поста

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import ru.netology.recipiesbook.Main.AdapterAndVMAllRecipes.RecipesViewModel
import ru.netology.recipiesbook.Main.AdapterAndVMContentRecipe.RecipeContentAdapter
import ru.netology.recipiesbook.Main.AdapterAndVMContentRecipe.RecipeContentViewModel
import ru.netology.recipiesbook.Main.AdapterAndVMSingleRecipe.SingleRecipeAdapter
import ru.netology.recipiesbook.Main.data.Recipe
import ru.netology.recipiesbook.Main.data.Repository.Companion.NEW_RECIPE_ID

import ru.netology.recipiesbook.databinding.RecipeContentFragmentBinding

//TODO тоже сделать через RcycleView, при нажатии "добавить" в массив шагов добавляется новый элемент,
// что заставляет перерисовывать разметку
// TODO реализовать сохранение предыдущих шагов при создании нового рецепта
class RecipeContentFragment : Fragment() {

    private val viewModel by viewModels<RecipeContentViewModel>(ownerProducer = ::requireParentFragment)
    private val args by navArgs<RecipeContentFragmentArgs>()

    // TODO Если рецепт ID новый, то... Посмотреть, как сделано в основном проекте.
    private val previousNewContent = context?.getSharedPreferences(
        "previousNewContent", Context.MODE_PRIVATE
    )
    private val currentId = args.recipeId

    val content = previousNewContent?.getString(SAVED_RECIPE_KEY, null)
    private val previousNewRecipeContent: Recipe? = if (content != null) {
        Json.decodeFromString(content)
    } else null

    private val currentRecipe = findRecipeById(currentId, viewModel.data.value!!)
        ?: previousNewRecipeContent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO может быть, перенести в onCreateView ? , менять currentRecipe в конце метода
        viewModel.deleteStepEvent.observe(this) {
            currentRecipe?.content?.removeAll() { recipestep ->
                recipestep.stepNumber == it
            }
            currentRecipe
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = RecipeContentFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        with(binding) {
            recipeName.setText(currentRecipe?.recipeName ?: FREE_SPACE)
            //TODO сделать выбор из ENUM ниже
            category.setText(currentRecipe?.category.toString())
//TODO Сделать добавление ячейки в массиве с пустыми значениями, менять currentRecipe в конце метода
            plusStepButton.setOnClickListener {

            }
        }

        val adapter = RecipeContentAdapter(viewModel)
        binding.recipeStepsContentFragment.adapter = adapter

        // TODO Обновлять список не при обновлении вью модели, а при обновлении текущего рецепта
        // TODO т.е. при удалении или добавлении шагов или при прописании полей названия или категории
        viewModel.data.observe(viewLifecycleOwner) {
            adapter.submitList(currentRecipe?.content)
            // метод вызывает обновление адаптера
        }


//TODO передавать массив по кнопке ok и обнулять currentRecipe
//        binding.ok.setOnClickListener {
//            if (!binding.recipeStepContent.editedText.text.isNullOrBlank()) {
//                val text = binding.recipeStepContent.editedText.text.toString()
//                viewModel.onSaveButtonClick(text)
//            }
//            findNavController().popBackStack() // уходим назад с этого фрагмента
//        }
    }.root

    companion object {
        const val REQUEST_KEY = "RequestKey"
        const val CONTENT_KEY = "PostContent"
        const val URL_KEY = "PostUrl"

        private const val SAVED_RECIPE_KEY = "PreviousNewRecipeContent"
        private const val FREE_SPACE = ""
    }
}
