package ru.netology.recipiesbook.Main

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
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

        setFragmentResultListener(FILTER_DIALOG_RESULT) { _, bundle ->
            val filterResult = bundle.getStringArrayList(SAVED_CATEGORIES_KEY)
            if (filterResult != null) {
                viewModel.filteredRecipeList.value = viewModel.data.value?.filter {
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

        adapter = RecipesAdapter(viewModel, viewModel.data.value as ArrayList<Recipe>)
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

//                        viewModel.filteredRecipeList.value = viewModel.filter(text)
                        return false
                    }

                    override fun onQueryTextChange(newText: String): Boolean {
                        //TODO почему не находит filter ?
//                        adapter.filter.filter
//                        viewModel.filteredRecipeList.value = viewModel.filter(newText)
                        return false
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
//                    R.id.actionSearch -> {
//                        val searchView: SearchView = menuItem.actionView as SearchView
//                        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
//                            android.widget.SearchView.OnQueryTextListener {
//                            override fun onQueryTextSubmit(text: String?): Boolean {
//                                if (text.isNullOrBlank()) return false
//                                viewModel.filteredRecipeList.value = viewModel.filter(text)
//                                return false
//                            }
//
//                            override fun onQueryTextChange(newText: String): Boolean {
//                                viewModel.filteredRecipeList.value = viewModel.filter(newText)
//                                return false
//                            }
//                        })
//                        true
//                    }

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

//        fun filter(text: String) {
//            val data: MutableList<Recipe> = viewModel.data.value?.toMutableList() ?: return
//            val filteredlist: ArrayList<Recipe> = ArrayList()
//
//            for (item in data) {
//                if (item.recipeName.toLowerCase().contains(text.toLowerCase())) {
//                    filteredlist.add(item)
//                }
//            }
//            if (filteredlist.isEmpty()) {
//                return
//            } else {
//                adapter.submitList(filteredlist)
//            }
//        }
    }
}

