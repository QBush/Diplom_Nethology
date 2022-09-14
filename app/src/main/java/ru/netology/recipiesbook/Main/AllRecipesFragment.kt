package ru.netology.recipiesbook.Main

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ListAdapter
import ru.netology.nmedia.adapter.RecipesAdapter
import ru.netology.recipiesbook.Main.AdapterAndVMAllRecipes.RecipesViewModel
import ru.netology.recipiesbook.Main.FilterDialogFragment.Companion.FILTER_DIALOG_RESULT
import ru.netology.recipiesbook.Main.FilterDialogFragment.Companion.SAVED_CATEGORIES_KEY
import ru.netology.recipiesbook.Main.data.Recipe
import ru.netology.recipiesbook.R
import ru.netology.recipiesbook.databinding.AllRecipesFragmentBinding

//Фрагмент для всех рецептов
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

        //слушатель для фильтрации
        setFragmentResultListener(FILTER_DIALOG_RESULT) { _, bundle ->
            val filterResult = bundle.getStringArrayList(SAVED_CATEGORIES_KEY)
            viewModel.choosenFilteredCategories.value = filterResult
            if (filterResult != null) {
                viewModel.filteredFavoriteRecipeList.value = viewModel.data.value
                    ?.filter {
                        filterResult.contains(it.category.toString())
                    }
            }
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

            if (viewModel.choosenFilteredCategories.value.isNullOrEmpty()) {
                adapter.submitList(it)
            } else {
                viewModel.filteredFavoriteRecipeList.value = viewModel.data.value
                    ?.filter {
                        viewModel.choosenFilteredCategories.value!!.contains(it.category.toString())
                    }
            }
        }

        binding.fab.setOnClickListener {
            viewModel.onAddClick()
        }

        //обработка поиска
        viewModel.filteredRecipeList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.toolbar_menu, menu)
                val search = menu.findItem(R.id.actionSearch)
                val searchView: SearchView = search.actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                    android.widget.SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(text: String?): Boolean {
                        if (text.isNullOrBlank()) return false
                        return false
                    }

                    override fun onQueryTextChange(newText: String): Boolean {
                        viewModel.filteredRecipeList.value = viewModel.filter(newText)
                        return false
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.filterDialogFragment -> {
                        val dialogFragment = FilterDialogFragment()
                        val manager = getFragmentManager()
                        if (manager != null) {
                            dialogFragment.show(manager, "filterDialog")
                        }
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }
}

