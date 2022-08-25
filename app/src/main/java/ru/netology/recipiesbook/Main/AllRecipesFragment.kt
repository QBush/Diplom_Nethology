package ru.netology.recipiesbook.Main

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.ThemedSpinnerAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.adapter.RecipesAdapter
import ru.netology.recipiesbook.Main.AdapterAndVMAllRecipes.RecipesViewModel
import ru.netology.recipiesbook.Main.AllRecipesFragmentDirections.Companion
import ru.netology.recipiesbook.Main.data.Recipe
import ru.netology.recipiesbook.databinding.AllRecipesFragmentBinding
import ru.netology.recipiesbook.databinding.AppActivityBinding

//TODO onLongClickListener
//TODO fab уменьшить в размерах так, чтобы значок внутри не сдвигался - читать в документации
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
            //TODO сюда поместить условие, фильтрованный список или нет и вставить функцию filter
            adapter.submitList(it) // метод вызывает обновление адаптера
        }

        binding.fab.setOnClickListener {
            viewModel.onAddClick()
        }

        fun filter(text: String, data: MutableList<Recipe>) {
            if (text.isEmpty()) return
            val filteredRecipes = mutableListOf<Recipe>()
            for (recipe in data) {
                if (recipe.recipeName.toLowerCase().contains(text.toLowerCase())
                ) {
                    filteredRecipes.add(recipe)
                }
            }
            adapter.submitList(filteredRecipes)
        }

    }.root



}