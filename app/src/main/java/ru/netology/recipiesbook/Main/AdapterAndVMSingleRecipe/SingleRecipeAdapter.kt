package ru.netology.recipiesbook.Main.AdapterAndVMSingleRecipe

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.netology.recipiesbook.Main.data.Recipe
import ru.netology.recipiesbook.Main.data.RecipeContent
import ru.netology.recipiesbook.R
import ru.netology.recipiesbook.databinding.RecipesListItemBinding
import ru.netology.recipiesbook.databinding.SingleRecipeStepBinding
internal class SingleRecipeAdapter(
) : ListAdapter<RecipeContent, SingleRecipeAdapter.ViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SingleRecipeStepBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: SingleRecipeStepBinding
    ) : RecyclerView.ViewHolder(binding.root) {

//        private lateinit var recipeContent: RecipeContent

        fun bind(recipeContent: RecipeContent) {
//            this.recipeContent = recipeContent
            with(binding) {
                stepText.text = recipeContent.stepContent
                if(!recipeContent.stepImageURL.isNullOrBlank()) {
                    Picasso.get().load(recipeContent.stepImageURL).into(binding.stepImage)
                }
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<RecipeContent>() {

        override fun areItemsTheSame(oldItem: RecipeContent, newItem: RecipeContent): Boolean =
            oldItem.stepNumber == newItem.stepNumber

        override fun areContentsTheSame(oldItem: RecipeContent, newItem: RecipeContent): Boolean =
            oldItem == newItem
    }
}