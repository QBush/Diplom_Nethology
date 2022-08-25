package ru.netology.recipiesbook.Main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.squareup.picasso.Picasso
import ru.netology.nmedia.adapter.RecipesAdapter
import ru.netology.recipiesbook.Main.AdapterAndVMSingleRecipe.SingleRecipeAdapter
import ru.netology.recipiesbook.Main.AdapterAndVMSingleRecipe.SingleRecipeViewModel
import ru.netology.recipiesbook.Main.data.Recipe
import ru.netology.recipiesbook.R
import ru.netology.recipiesbook.databinding.SingleRecipeFragmentBinding

class SingleRecipeFragment : Fragment() {

    private val viewModel by viewModels<SingleRecipeViewModel>(ownerProducer = ::requireParentFragment)
    private val args by navArgs<SingleRecipeFragmentArgs>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.navigateToRecipeContentFragmentFromSingleRecipe.observe(this) {
            val direction = AllRecipesFragmentDirections.toRecipeContentFragment(it)
            findNavController().navigate(direction)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = SingleRecipeFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        val currentId = args.recipeId
        val currentRecipeList = viewModel.data.value

        var currentRecipe = findRecipeById(currentId, currentRecipeList)
            ?: run {
                findNavController().popBackStack()
                return@also
            }
        viewModel.currentRecipe.value = currentRecipe

        val popupMenu by lazy {
            PopupMenu(context, binding.singleListItem.options).apply {
                inflate(R.menu.options_recipe)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.remove -> {
                            viewModel.onRemoveClick(currentRecipe.recipeId)
                            true
                        }
                        R.id.edit -> {
                            viewModel.onEditClick(currentRecipe)
                            true
                        }
                        else -> false
                    }
                }
            }
        }

        with(binding) {
            singleListItem.recipeName.text = currentRecipe.recipeName
            singleListItem.category.text = currentRecipe.category.toString()
            singleListItem.authorName.text = currentRecipe.author
            singleListItem.options.setOnClickListener { popupMenu.show() }
        }

        val adapter = SingleRecipeAdapter()
        binding.recipeStepsContentRecyclerView.adapter = adapter

        viewModel.currentRecipe.observe(viewLifecycleOwner) {
            val recipe = it
            adapter.submitList(recipe.content)
        }
    }.root

}