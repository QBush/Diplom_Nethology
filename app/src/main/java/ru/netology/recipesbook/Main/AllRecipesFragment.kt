package ru.netology.recipesbook.Main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.adapter.RecipeAdapter
import ru.netology.recipesbook.Main.adapterAndViewmodel.RecipesViewModel
import ru.netology.recipesbook.databinding.AllRecipesFragmentBinding

class AllRecipesFragment: Fragment()  {

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

        val adapter = RecipeAdapter(viewModel)
        binding.PostsRecycleView.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) {
            adapter.submitList(it) // метод вызывает обновление адаптера
        }

        binding.fab.setOnClickListener {
            viewModel.onAddClick()
        }

    }.root
}