package ru.netology.recipiesbook.Main
// Активити для редактирования и создания поста

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.squareup.picasso.Picasso
import ru.netology.recipiesbook.Main.AdapterAndVMAllRecipes.RecipesViewModel
import ru.netology.recipiesbook.databinding.RecipeContentFragmentBinding

class RecipeContentFragment : Fragment() {


    private val viewModel by viewModels<RecipesViewModel>(ownerProducer = ::requireParentFragment)
    private val args by navArgs<RecipeContentFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = RecipeContentFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        val currentId = args.recipeId

        val currentRecipe = findRecipeById(currentId, viewModel.data.value!!)
            ?: run {
                findNavController().popBackStack()
                return@also
            }

//TODO сделать количество полей ниже столько, сколько шагов в рецепте
        binding.recipeStepContent.stepText.requestFocus()
        binding.recipeStepContent.stepText.setText(currentRecipe.content[0].stepContent)
        binding.recipeStepContent.stepImage.setText(currentRecipe.content[0].stepImageURL)


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
