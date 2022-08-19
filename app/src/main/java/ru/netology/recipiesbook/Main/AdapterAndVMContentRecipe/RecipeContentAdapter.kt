package ru.netology.recipiesbook.Main.AdapterAndVMContentRecipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.netology.nmedia.adapter.RecipeInteractionListener
import ru.netology.recipiesbook.Main.AdapterAndVMSingleRecipe.SingleRecipeAdapter
import ru.netology.recipiesbook.Main.data.RecipeContent
import ru.netology.recipiesbook.databinding.RecipeStepContentBinding
import ru.netology.recipiesbook.databinding.SingleRecipeStepBinding

internal class RecipeContentAdapter(
    private val interactionListener: RecipeContentListener
) :
    ListAdapter<RecipeContent, RecipeContentAdapter.ViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecipeStepContentBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: RecipeStepContentBinding,
        private val interactionListener: RecipeContentListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var recipeContent: RecipeContent

        // TODO доделать удаление шага до конца - см фргмент   deleteStepEvent
        init {
            binding.deleteStepButton.setOnClickListener {
                interactionListener.onDeleteStepClick(recipeContent.stepNumber)
            }
            binding.stepImage.showSoftInputOnFocus = false
        }

        fun bind(recipeContent: RecipeContent) {
            this.recipeContent = recipeContent
            with(binding) {
                stepText.setText(recipeContent.stepContent)
                stepImage.setText(recipeContent.stepImageURL)
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