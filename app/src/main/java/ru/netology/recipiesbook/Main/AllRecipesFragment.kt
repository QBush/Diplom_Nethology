package ru.netology.recipiesbook.Main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.netology.nmedia.adapter.RecipeAdapter
import ru.netology.recipiesbook.Main.adapterAndViewmodel.RecipesViewModel
import ru.netology.recipiesbook.databinding.AllRecipesFragmentBinding

class AllRecipesFragment: Fragment()  {

    private val viewModel by viewModels<RecipesViewModel>

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

    }.root
}