package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.netology.recipiesbook.Main.data.Recipe
import ru.netology.recipiesbook.R
import ru.netology.recipiesbook.databinding.RecipesListItemBinding

internal class RecipesAdapter(
    private val interactionListener: RecipeInteractionListener

) : ListAdapter<Recipe, RecipesAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecipesListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class ViewHolder(
        private val binding: RecipesListItemBinding,
        private val interactionListener: RecipeInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var recipe: Recipe

        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.options).apply {
                inflate(R.menu.options_recipe)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.remove -> {
                            interactionListener.onRemoveClick(recipe.recipeId)
                            true
                        }
                        R.id.edit -> {
                            interactionListener.onEditClick(recipe)
                            true
                        }
                        else -> false
                    }
                }
            }
        }

        init {
            binding.recipeName.setOnClickListener { interactionListener.onContentClick(recipe) }
            binding.addToFavorites.setOnClickListener {
                interactionListener.onAddToFavoritesClick(recipe.recipeId)
            }
            binding.options.setOnClickListener { popupMenu.show() }
        }

        fun bind(recipe: Recipe) {
            this.recipe = recipe
            with(binding) {
                category.text = recipe.category.toString()
                authorName.text = recipe.author
                recipeName.text = recipe.recipeName
                if (!recipe.mainImageSource.isNullOrBlank()) {
                    Picasso.get().load(recipe.mainImageSource).into(binding.mainRecipeImage)
                }
                addToFavorites.isChecked = recipe.addedToFavorites
            }
        }
    }


    // для сравнения объектов через ListAdapter
    private object DiffCallback : DiffUtil.ItemCallback<Recipe>() {

        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean =
            oldItem.recipeId == newItem.recipeId


        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean =
            oldItem == newItem
    }
}