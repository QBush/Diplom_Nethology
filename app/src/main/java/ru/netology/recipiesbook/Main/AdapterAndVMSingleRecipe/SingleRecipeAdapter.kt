package ru.netology.recipiesbook.Main.AdapterAndVMSingleRecipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.netology.recipiesbook.Main.data.RecipeContent
import ru.netology.recipiesbook.databinding.SingleRecipeStepBinding

internal class SingleRecipeAdapter(
) : ListAdapter<RecipeContent, SingleRecipeAdapter.ViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
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

        fun bind(recipeContent: RecipeContent) {
            with(binding) {
                stepText.text = recipeContent.stepContent
                if (!recipeContent.stepImageURL.isNullOrBlank()) {
                    Picasso.get().load(recipeContent.stepImageURL)
                        .fit()
                        .into(binding.stepImage)

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