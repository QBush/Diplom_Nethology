package ru.netology.recipesbook.Main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.adapter.RecipeAdapter
import ru.netology.recipesbook.Main.adapterAndViewmodel.RecipesViewModel
import ru.netology.recipesbook.Main.data.Recipe
import ru.netology.recipesbook.databinding.AllRecipesFragmentBinding

class FavoriteRecipesFragment: Fragment()  {

    private val viewModel by viewModels<RecipesViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.navigateToNewRecipeFragment.observe(this) {
            val direction = AllRecipesFragmentDirections.toRecipeContentFragment(it)
            findNavController().navigate(direction)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = AllRecipesFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        var favorites: List<Recipe>?

        val adapter = RecipeAdapter(viewModel)
        binding.PostsRecycleView.adapter = adapter

        // здесь фильтруем список, все остальное так же
        viewModel.data.observe(viewLifecycleOwner) {
            favorites = viewModel.data.value?.filter { it.addedToFavorites }
            adapter.submitList(favorites) // метод вызывает обновление адаптера
        }

        binding.fab.setOnClickListener {
            viewModel.onAddClick()
        }

    }.root
}