package ru.netology.recipiesbook.Main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.adapter.RecipesAdapter

import ru.netology.recipiesbook.Main.AdapterAndVMAllRecipes.RecipesViewModel

import ru.netology.recipiesbook.Main.data.Recipe
import ru.netology.recipiesbook.databinding.AllRecipesFragmentBinding
import ru.netology.recipiesbook.databinding.FavoritesRecipesFragmentBinding

class FavoriteRecipesFragment : Fragment() {

    private val viewModel by viewModels<RecipesViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.navigateToRecipeContentFragmentFromAllRecipes.observe(this) {
            val direction = AllRecipesFragmentDirections.toRecipeContentFragment(it)
            findNavController().navigate(direction)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FavoritesRecipesFragmentBinding.inflate(layoutInflater, container, false).also { binding ->



        val adapter = RecipesAdapter(viewModel)
        binding.PostsRecycleView.adapter = adapter

        // здесь фильтруем список, все остальное так же
        viewModel.data.observe(viewLifecycleOwner) { it ->
            val favorites = it.filter { it.addedToFavorites }
            adapter.submitList(favorites)

            if (viewModel.data.value?.firstOrNull {it.addedToFavorites} == null) {
                binding.favoriteRecipesFullPicture.visibility = View.VISIBLE
            } else {
                binding.favoriteRecipesFullPicture.visibility = View.GONE
            }

        }

    }.root
}