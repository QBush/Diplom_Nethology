package ru.netology.recipiesbook.Main.AdapterAndVMContentRecipe

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.recipiesbook.Main.data.RecipeContent
import ru.netology.recipiesbook.R
import ru.netology.recipiesbook.databinding.RecipeStepContentBinding

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

        init {
            binding.deleteStepButton.setOnClickListener {
                interactionListener.onDeleteStepClick(recipeContent.stepNumber)
            }
            binding.stepImage.showSoftInputOnFocus = false
            binding.saveStepButton.setOnClickListener {
                interactionListener.onSaveStepClick(recipeContent)
            }
        }


        @SuppressLint("ResourceAsColor")
        fun bind(recipeContent: RecipeContent) {
            this.recipeContent = recipeContent
            with(binding) {
                stepText.setText(recipeContent.stepContent)
                stepImage.setText(recipeContent.stepImageURL)
                val backgroundColor = if (recipeContent.saved) R.color.teal_700
                else R.color.transparent_white
                binding.saveStepButton.setBackgroundColor(backgroundColor)
            }
            //TODO вставить блокировку текста от редактирования

    }
}


private object DiffCallback : DiffUtil.ItemCallback<RecipeContent>() {

    override fun areItemsTheSame(oldItem: RecipeContent, newItem: RecipeContent): Boolean =
        oldItem.stepNumber == newItem.stepNumber

    override fun areContentsTheSame(oldItem: RecipeContent, newItem: RecipeContent): Boolean =
        oldItem == newItem
}
}