package ru.netology.recipiesbook.Main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.netology.recipiesbook.Main.utils.BottomBarHideInterface
import ru.netology.recipiesbook.Main.utils.SingleLiveEvent
import ru.netology.recipiesbook.R

class AppActivity : AppCompatActivity(R.layout.app_activity), BottomBarHideInterface{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.bottom_nav)
            .setupWithNavController(navController)

        val bottomNavEvent = SingleLiveEvent<Int>()

    }
}