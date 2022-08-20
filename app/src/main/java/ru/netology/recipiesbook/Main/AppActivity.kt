package ru.netology.recipiesbook.Main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.netology.recipiesbook.Main.utils.BottomBarHideInterface
import ru.netology.recipiesbook.Main.utils.SingleLiveEvent
import ru.netology.recipiesbook.R
import ru.netology.recipiesbook.databinding.AppActivityBinding

class AppActivity : AppCompatActivity(R.layout.app_activity), BottomBarHideInterface {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.bottom_nav)
            .setupWithNavController(navController)
    }

    // метод скрытия bottomBar
    fun hideBottomBar(hide: Boolean) {
        val binding = AppActivityBinding.inflate(layoutInflater)
        if (hide) binding.bottomNav.visibility = View.GONE
        else binding.bottomNav.visibility = View.VISIBLE

    }
}