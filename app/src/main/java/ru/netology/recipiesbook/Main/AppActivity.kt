package ru.netology.recipiesbook.Main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.netology.recipiesbook.Main.utils.SingleLiveEvent
import ru.netology.recipiesbook.R
import ru.netology.recipiesbook.databinding.AppActivityBinding

class AppActivity : AppCompatActivity(R.layout.app_activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = AppActivityBinding.inflate(layoutInflater)

        //устанавливаем нижнюю навигацию
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.bottom_nav)
            .setupWithNavController(navController)

        val toolbarBinding = findViewById<BottomNavigationView>(R.id.bottom_nav)
        //ToolBar
        setSupportActionBar(findViewById(R.id.topAppBar))

        navController.addOnDestinationChangedListener { _, nd: NavDestination, _ ->
            if (nd.id == R.id.allRecipesFragment || nd.id == R.id.favoritesRecipesFragment) {
                toolbarBinding.visibility = View.VISIBLE
            } else {
                toolbarBinding.visibility = View.GONE
            }
        }


    }

}


