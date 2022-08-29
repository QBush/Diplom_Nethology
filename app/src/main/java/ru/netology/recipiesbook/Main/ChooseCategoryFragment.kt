package ru.netology.recipiesbook.Main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import ru.netology.recipiesbook.R
import ru.netology.recipiesbook.databinding.AppActivityBinding

class ChooseCategoryFragment: DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.filter_dialog, container)




        return super.onCreateView(inflater, container, savedInstanceState)
    }
}