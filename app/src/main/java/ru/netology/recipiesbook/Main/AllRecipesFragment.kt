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
            val filterResult = bundle.get(SAVED_CATEGORIES_KEY) as Array<String>
            viewModel.filteredRecipeList.value = viewModel.data.value?.filter {
                filterResult.contains(it.category.toString())
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
            adapter.submitList(it)
        }

        binding.fab.setOnClickListener {
            viewModel.onAddClick()
        }

        viewModel.filteredRecipeList.observe(viewLifecycleOwner) {
            if(!it.isNullOrEmpty()) {
                adapter.submitList(it)
            }
        }

    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.toolbar_menu, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.actionSearch -> {
                        // TODO фильтрации не происходит, вообще в код не заходит при клике на поиск
                        val searchView: SearchView = menuItem.actionView as SearchView
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
                        true
                    }
                    R.id.filterDialogFragment -> {
                        //TODO при переходе на диалог обваливается
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

// TODO ниже другая попытка реанимировать меню в ToolBar - не отображаются кнопки меню

//    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
//        R.id.actionSearch -> {
//            val searchView: SearchView = item.actionView as SearchView
//
//            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
//                android.widget.SearchView.OnQueryTextListener {
//                override fun onQueryTextSubmit(text: String?): Boolean {
//                    if (text.isNullOrBlank()) return false
//                    adapter.submitList(filter(text))
//                    return false
//                }
//
//                override fun onQueryTextChange(newText: String): Boolean {
//                    adapter.submitList(filter(newText))
//                    return false
//                }
//            })
//            true
//        }
//        R.id.filterDialogFragment -> {
//            val dialogFragment = FilterDialogFragment()
//            val manager = getFragmentManager()
//            if (manager != null) {
//                dialogFragment.show(manager, "filterDialog")
//            }
//            true
//        }
//        else -> {
//            super.onOptionsItemSelected(item)
//        }
//    }

    //TODO Еще одна попытка: кнопки меню не отображаются во фрагменте, если делать в Action Bar
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.toolbar_menu, menu)
//        val searchItem: MenuItem = menu.findItem(R.id.actionSearch)
//        val searchView: SearchView = searchItem.actionView as SearchView
//
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
//            android.widget.SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(text: String?): Boolean {
//                if (text.isNullOrBlank()) return false
//                adapter.submitList(filter(text))
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String): Boolean {
//                adapter.submitList(filter(newText))
//                return false
//            }
//        })
//        return super.onCreateOptionsMenu(menu, inflater)
//    }

    // метод для фильтра по поисковому запросу
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