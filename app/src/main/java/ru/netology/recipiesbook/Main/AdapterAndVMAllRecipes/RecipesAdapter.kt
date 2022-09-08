package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.PopupMenu
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.*
import ru.netology.recipiesbook.Main.data.Recipe
import ru.netology.recipiesbook.R
import ru.netology.recipiesbook.databinding.RecipesListItemBinding

class RecipesAdapter(
    private val interactionListener: RecipeInteractionListener,
    private var recipeList: ArrayList<Recipe>
) : ListAdapter<Recipe, RecipesAdapter.ViewHolder>(DiffCallback), Filterable {

    var recipeFilterList = ArrayList<Recipe>()

    init {
        recipeFilterList = recipeList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecipesListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recipeFilterList[position])
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
                    binding.mainRecipeImage.visibility = View.VISIBLE
                    //TODO картинка по ссылке не отображается
//                    mainRecipeImage.setImageURI(recipe.mainImageSource.toUri())
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

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    recipeFilterList = recipeList
                } else {
                    val resultList = ArrayList<Recipe>()
                    for (recipe in recipeList) {
                        if (recipe.recipeName.toLowerCase().contains(charSearch.toLowerCase())) {
                            resultList.add(recipe)
                        }
                    }
                    recipeFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = recipeFilterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                recipeFilterList = results?.values as ArrayList<Recipe>
                notifyDataSetChanged()
            }
        }
    }
}
