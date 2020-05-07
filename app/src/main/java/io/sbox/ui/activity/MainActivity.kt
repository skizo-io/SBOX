package io.sbox.ui.activity

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import io.sbox.R
import io.sbox.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.activity_main_container)
//        val appBarConfiguration = AppBarConfiguration(navController.graph)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_daily,
                R.id.navigation_ranking
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        activity_main_navigation.setupWithNavController(navController)


//        activity_main_navigation.setOnNavigationItemSelectedListener { item ->
//            replaceFragment(item.itemId)
//        }

    }


}


