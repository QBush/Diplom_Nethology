package ru.netology.recipiesbook.Main
// Активити для редактирования и создания поста

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.recipiesbook.Main.adapterAndViewmodel.RecipesViewModel
import ru.netology.recipiesbook.databinding.RecipeContentFragmentBinding

class RecipeContentFragment : Fragment() {

    // в скобках owner
    private val viewModel by viewModels<RecipesViewModel>()
    private val args by navArgs<RecipeContentFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = RecipeContentFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        binding.editedText.requestFocus()
        binding.editedText.setText(args.initialContent?.content)

        binding.ok.setOnClickListener {
            if (!binding.editedText.text.isNullOrBlank()) {
                val text = binding.editedText.text.toString()
                viewModel.onSaveButtonClick(text)
            }
            findNavController().popBackStack() // уходим назад с этого фрагмента
        }
    }.root

    companion object {
        const val REQUEST_KEY = "RequestKey"
        const val CONTENT_KEY = "PostContent"
        const val URL_KEY = "PostUrl"

//        private const val INITIAL_TEXT_KEY = "PostInitialText"
//        private const val INITIAL_URL_KEY = "PostInitialUrl"


        // функция, которую мы вызываем для создания Фрагмента. Потом из аргументов достаем данные (см onCreateView)
//        fun create(initialContent: PostEditableContent?) = PostContentFragment().apply {
//            arguments = createBundle(initialContent)
//        }
//
//        fun createBundle(initialContent: PostEditableContent?) = Bundle(2).apply {
//            putString(INITIAL_TEXT_KEY, initialContent?.content)
//            putString(INITIAL_URL_KEY, initialContent?.videoUrl)
//        }
    }
}
