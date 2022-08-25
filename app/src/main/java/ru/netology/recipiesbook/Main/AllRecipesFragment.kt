package ru.netology.recipiesbook.Main

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.ThemedSpinnerAdapter
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.adapter.RecipesAdapter
import ru.netology.recipiesbook.Main.AdapterAndVMAllRecipes.RecipesViewModel
import ru.netology.recipiesbook.Main.AllRecipesFragmentDirections.Companion
import ru.netology.recipiesbook.Main.data.Recipe
import ru.netology.recipiesbook.databinding.AllRecipesFragmentBinding
import ru.netology.recipiesbook.databinding.AppActivityBinding

class AllRecipesFragment: Fragment()  {


    private val viewModel by viewModels<RecipesViewModel>(ownerProducer = ::requireParentFragment)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.navigateToRecipeContentFragmentFromAllRecipes.observe(this) {
            val direction = AllRecipesFragmentDirections.toRecipeContentFragment(it)
            findNavController().navigate(direction)
        }

        viewModel.navigateToSingleRecipeFragment.observe(this) {
            val direction = AllRecipesFragmentDirections.toSingleRecipeFragment(it)
            findNavController().navigate(direction)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = AllRecipesFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        val adapter = RecipesAdapter(viewModel)
        binding.PostsRecycleView.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.allRecipesFullPicture.visibility = View.VISIBLE
            } else binding.allRecipesFullPicture.visibility = View.GONE
            if (binding.search.isEmpty()) {
                adapter.submitList(it)
            }
        }

        binding.fab.setOnClickListener {
            viewModel.onAddClick()
        }

//TODO не могу нажать на search
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String?): Boolean {
                if (text.isNullOrBlank()) return false
                adapter.submitList(filter(text))
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) return false
                adapter.submitList(filter(newText))
                return false
            }
        })

    }.root

    fun filter(text: String): MutableList<Recipe>? {
        val filteredRecipes = viewModel.data.value?.toMutableList() ?: return null
        for (recipe in filteredRecipes) {
            if (recipe.recipeName.toLowerCase().contains(text.toLowerCase())
            ) {
                filteredRecipes.add(recipe)
            }
        }
        return filteredRecipes
    }
}