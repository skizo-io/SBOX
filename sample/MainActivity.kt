package com.smackjeeves.ui.activity

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import com.smackjeeves.R
import com.smackjeeves.model.ui.ComicMainTab
import com.smackjeeves.network.Api
import com.smackjeeves.network.send
import com.smackjeeves.preferences.AppPreference
import com.smackjeeves.ui.base.BaseActivity
import com.smackjeeves.ui.bookshelf.BookshelfMainFragment
import com.smackjeeves.ui.main.ComicMainFragment
import com.smackjeeves.ui.article.ComicTitleActivity
import com.smackjeeves.ui.article.sub.select.SelectDownloadFragment
import com.smackjeeves.ui.home.HomeFragment
import com.smackjeeves.ui.menu.MenuFragment
import com.smackjeeves.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import skizo.library.extensions.getColorFromRes


class MainActivity : BaseActivity() {


    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (AppPreference.showOnboarding) {
            newActivity<OnboardingActivity>()
            AppPreference.showOnboarding = false
        }

        main_navigation.setOnNavigationItemSelectedListener { item ->
            replaceFragment(item.itemId)
        }

        main_navigation.itemIconTintList = ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_checked),
                intArrayOf(android.R.attr.state_checked)
            ),
            intArrayOf(getColorFromRes(R.color.dark_grey), getColorFromRes(R.color.black))
        )

        createTabList()

        main_navigation.selectedItemId = R.id.navigation_main_exclusive


        // TODO : test code
        newActivity<ComicTitleActivity>("id".to(90), "isDiscover".to(false))
//        newFragment<SelectDownloadFragment>(SelectDownloadFragment.getBundle(64, listOf(2,3,4)))

//        newFragment<SelectArticleFragment>(SelectArticleFragment.getBundle(90, false))
//        newFragment<SelectPurchaseFragment>(SelectPurchaseFragment.getBundle("https://alpha-images.smackjeeves.com/article/64/1/thumbnail/64_1_SMALL_1560494929018.jpg", listOf(5,6,8,9,12,15,21), 555))

//        newActivity<ViewerActivity>("titleId".to(90), "articleId".to(1))
//        main_navigation.selectedItemId = R.id.navigation_main_menu


    }


    private fun createTabList() {
        Config.comicMainTab.daily.clear()
        Config.comicMainTab.ranking.clear()
        Config.comicMainTab.genres.clear()

        resources.getStringArray(R.array.daily_tab_categories).forEach {
            Config.comicMainTab.daily.add(
                ComicMainTab(
                    it.split(",")[1],
                    it.split(",")[0]
                )
            )
        }
        resources.getStringArray(R.array.ranking_tab_categories).forEach {
            Config.comicMainTab.ranking.add(
                ComicMainTab(
                    it.split(",")[1],
                    it.split(",")[0]
                )
            )
        }
        Api.service.getGenre().send()
    }


    private fun replaceFragment(itemId: Int): Boolean {
        val tag = itemId.toString()

        var fragment = supportFragmentManager.findFragmentByTag(tag) ?: when (itemId) {
//        when (itemId) {
            R.id.navigation_main_home -> HomeFragment.newInstance()
            R.id.navigation_main_exclusive -> ComicMainFragment.newInstance(ComicMainFragment.PageType.EXCLUSIVE_DAILY)
            R.id.navigation_main_discover -> ComicMainFragment.newInstance(ComicMainFragment.PageType.DISCOVER_POPULAR)
            R.id.navigation_main_bookshelf -> BookshelfMainFragment.newInstance(
                BookshelfMainFragment.PageType.FAVORITE
            )
            R.id.navigation_main_menu -> MenuFragment.newInstance()
            else -> null
        }

        fragment?.let {
            val transaction = supportFragmentManager.beginTransaction()
            if (it.isAdded) transaction.show(it).commit()
            else transaction.replace(R.id.main_container, it, tag).commit()
            return true
        }
        return false
    }


}
