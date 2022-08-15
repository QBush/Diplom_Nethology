package ru.netology.recipiesbook.Main
// Активити для редактирования и создания поста

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.recipiesbook.Main.AdapterAndVMAllRecipes.RecipesViewModel
import ru.netology.recipiesbook.Main.AdapterAndVMContentRecipe.RecipeContentAdapter
import ru.netology.recipiesbook.Main.AdapterAndVMContentRecipe.RecipeContentViewModel
import ru.netology.recipiesbook.Main.AdapterAndVMSingleRecipe.SingleRecipeAdapter

import ru.netology.recipiesbook.databinding.RecipeContentFragmentBinding

//TODO тоже сделать через RcycleView, при нажатии "добавить" в массив шагов добавляется новый элемент,
// что заставляет перерисовывать разметку
class RecipeContentFragment : Fragment() {

    private val viewModel by viewModels<RecipeContentViewModel>(ownerProducer = ::requireParentFragment)
    private val args by navArgs<RecipeContentFragmentArgs>()

    private val currentId = args.recipeId
// TODO Если рецепт ID новый, то... Посмотреть, как сделано в основном проекте.

    // TODO Здесь если рецепта нет(то есть пришли через плюс), то одни действия,
    // TODO если есть(пришли через редактирование), то другие. Посмотреть основной проект
    private val currentRecipe = findRecipeById(currentId, viewModel.data.value!!)
        ?: // TODO новый рецепт

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO может быть, перенести в onCreateView ?
        viewModel.deleteStepEvent.observe(this) {
            currentRecipe
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = RecipeContentFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        with(binding) {
            recipeName.setText(currentRecipe.recipeName)
            category.setText(currentRecipe.category.toString())
            //TODO Сделать добавление ячейки в массиве с пустыми значениями
            plusStepButton.setOnClickListener {

            }
        }

        binding.recipeName.setText(currentRecipe.recipeName)
        //TODO сделать выбор из ENUM ниже
        binding.category.setText(currentRecipe.category.toString())

        val adapter = RecipeContentAdapter()
        binding.recipeStepsContentFragment.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) {
            adapter.submitList(findRecipeById(currentId,it)?.content)
        // метод вызывает обновление адаптера
        }


//TODO передавать массив по кнопке ok
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

    }
}
