package io.sbox.ui.activity

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.core.view.forEach
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import io.comico.library.extensions.getColorFromRes
import io.comico.library.extensions.showToast
import io.comico.library.extensions.todo
import io.comico.library.extensions.trace
import io.comico.ui.main.home.HomeFragment
import io.sbox.R
import io.sbox.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val states = arrayOf(
            intArrayOf(-android.R.attr.state_checked),
            intArrayOf(android.R.attr.state_checked)
        )
        val colors = intArrayOf(getColorFromRes(R.color.gray04), getColorFromRes(R.color.black))
        activity_main_navigation.itemIconTintList = ColorStateList(states, colors)
        activity_main_navigation.itemTextColor = ColorStateList(states, colors)

        activity_main_navigation.setOnNavigationItemSelectedListener {
            replaceFragment(it)
        }

        activity_main_navigation.selectedItemId = R.id.navigation_home







    }




    private fun replaceFragment(item: MenuItem): Boolean {

        activity_main_navigation.menu.forEach {
            it.isEnabled = !(it == item)
        }

        val itemId = item.itemId
        val tag = itemId.toString()

        var fragment = supportFragmentManager.findFragmentByTag(tag) ?: when (itemId) {
            R.id.navigation_home -> {
                HomeFragment()
            }
            R.id.navigation_daily -> {
                HomeFragment()
            }
            R.id.navigation_ranking -> {
                HomeFragment()
            }
            else -> null
        }

        showToast("Bottom Navi $itemId $tag")

        fragment?.let {
            val transaction = supportFragmentManager.beginTransaction()
            if (it.isAdded) transaction.show(it).commit()
            else transaction.replace(R.id.activity_main_container, it, tag).commit()
            return true
        }
        return false
    }


}


