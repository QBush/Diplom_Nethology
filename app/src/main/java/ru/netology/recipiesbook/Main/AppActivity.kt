package ru.netology.recipiesbook.Main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
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


