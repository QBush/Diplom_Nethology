package ru.netology.recipiesbook.Main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.squareup.picasso.Picasso
import ru.netology.recipiesbook.Main.AdapterAndVMAllRecipes.RecipesViewModel
import ru.netology.recipiesbook.databinding.SingleRecipeFragmentBinding

class SingleRecipeFragment : Fragment() {

    private val viewModel by viewModels<RecipesViewModel>(ownerProducer = ::requireParentFragment)
    private val args by navArgs<SingleRecipeFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = SingleRecipeFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        val currentId = args.recipeId

        val currentRecipe = findRecipeById(currentId, viewModel.data.value!!)
            ?: run {
                findNavController().popBackStack()
                return@also
            }

        with(binding) {
            recipeName.text = currentRecipe.recipeName
            category.text = currentRecipe.category.toString()
            authorName.text = currentRecipe.author
            //TODO сделать количество полей ниже столько, сколько шагов в рецепте
            Picasso.get().load(currentRecipe.content[0].stepImageURL).into(singleRecipeStep.stepImage)
            singleRecipeStep.stepText.text = currentRecipe.content[0].stepContent
        }

    }.root
}