package ru.netology.recipiesbook.Main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.ThemedSpinnerAdapter
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ListAdapter
import ru.netology.nmedia.adapter.RecipesAdapter
import ru.netology.recipiesbook.Main.AdapterAndVMAllRecipes.RecipesViewModel
import ru.netology.recipiesbook.Main.AllRecipesFragmentDirections.Companion
import ru.netology.recipiesbook.Main.data.Recipe
import ru.netology.recipiesbook.R
import ru.netology.recipiesbook.databinding.AllRecipesFragmentBinding
import ru.netology.recipiesbook.databinding.AppActivityBinding

class AllRecipesFragment : Fragment() {


    private val viewModel by viewModels<RecipesViewModel>(ownerProducer = ::requireParentFragment)
    lateinit var adapter: ListAdapter<Recipe, RecipesAdapter.ViewHolder>

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

        adapter = RecipesAdapter(viewModel)
        binding.PostsRecycleView.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.allRecipesFullPicture.visibility = View.VISIBLE
            } else binding.allRecipesFullPicture.visibility = View.GONE
            adapter.submitList(it)
        }

        binding.fab.setOnClickListener {
            viewModel.onAddClick()
        }


//TODO searchView не кликабельно

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

    //TODO поиск не отображается
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val searchItem: MenuItem = menu.findItem(R.id.actionSearch)
        val searchView: SearchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String?): Boolean {
                if (text.isNullOrBlank()) return false
                adapter.submitList(filter(text))
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.submitList(filter(newText))
                return false
            }
        })
        return super.onCreateOptionsMenu(menu, inflater)
    }

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