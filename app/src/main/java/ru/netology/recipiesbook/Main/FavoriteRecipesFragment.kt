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
import ru.netology.recipiesbook.Main.FilterDialogFragment.Companion.SAVED_CATEGORIES_KEY
import ru.netology.recipiesbook.Main.data.Recipe
import ru.netology.recipiesbook.R
import ru.netology.recipiesbook.databinding.FavoritesRecipesFragmentBinding

//Фрагмент для избранных рецептов. Почти аналогичен фрагменту всех рецептов
class FavoriteRecipesFragment : Fragment() {

    private val viewModel by viewModels<RecipesViewModel>(ownerProducer = ::requireParentFragment)
    lateinit var adapter: ListAdapter<Recipe, RecipesAdapter.ViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.navigateToRecipeContentFragmentFromAllRecipes.observe(this) {
            val direction = FavoriteRecipesFragmentDirections.toRecipeContentFragment(it)
            findNavController().navigate(direction)
        }

        viewModel.navigateToSingleRecipeFragment.observe(this) {
            val direction = FavoriteRecipesFragmentDirections.toSingleRecipeFragment(it)
            findNavController().navigate(direction)
        }

        setFragmentResultListener(FilterDialogFragment.FILTER_DIALOG_RESULT) { _, bundle ->
            val filterResult = bundle.getStringArrayList(SAVED_CATEGORIES_KEY)
            if (filterResult != null) {
                viewModel.filteredFavoriteRecipeList.value = viewModel.data.value
                    ?.filter {
                        filterResult.contains(it.category.toString()) &&
                                it.addedToFavorites
                    }

            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FavoritesRecipesFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        adapter = RecipesAdapter(viewModel)
        binding.PostsRecycleView.adapter = adapter

        // здесь фильтруем список, все остальное так же
        viewModel.data.observe(viewLifecycleOwner) { it ->
            val favorites = it.filter { it.addedToFavorites }
            if (favorites.isEmpty()) {
                binding.favoriteRecipesFullPicture.visibility = View.VISIBLE
            } else {
                binding.favoriteRecipesFullPicture.visibility = View.GONE
            }
            adapter.submitList(favorites)
        }
//обработка поиска
        viewModel.filteredFavoriteRecipeList.observe(viewLifecycleOwner) {
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
                        viewModel.filteredFavoriteRecipeList.value =
                            viewModel.filterFavorite(newText)?.filter {
                                it.addedToFavorites
                            }
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