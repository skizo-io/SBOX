package com.smackjeeves.ui.menu.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.smackjeeves.R
import com.smackjeeves.model.NewsCategories
import com.smackjeeves.network.Api
import com.smackjeeves.network.send
import com.smackjeeves.ui.base.BaseFragment
import kotlinx.android.synthetic.main.common_viewpager.*
import kotlinx.android.synthetic.main.item_appbar.*


class NewsMainFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle? = null) =
            NewsMainFragment().apply {
                arguments = bundle
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.common_viewpager, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        appbar_title.setText(R.string.site_news)
        appbar.setVisible(back = true)

        Api.service.getNewsCategories().send(
            this::createCategories
        )

    }

    fun createCategories(vo: NewsCategories) {

        appbar.visibleTabbar = true
        var categories = mutableListOf<String>()
        vo.data.categories.forEach {
            appbar_tabbar.addTab(appbar_tabbar.newTab().setText(it.name))
            categories.add(it.key)
        }

        viewpager.offscreenPageLimit = vo.data.categories.size
        viewpager.adapter = NewsPageAdapter(childFragmentManager, categories)
        viewpager.adapter?.notifyDataSetChanged()

        appbar_tabbar.tabMode = TabLayout.MODE_FIXED
        appbar_tabbar.setupWithViewPager(viewpager)
        appbar_tabbar.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
            override fun onTabSelected(tab: TabLayout.Tab?) {
            }
        })


    }

    class NewsPageAdapter(fm: FragmentManager, var categories: List<String>) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return NewsContentFragment.newInstance(categories.get(position))
        }

        override fun getCount(): Int {
            return categories.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return categories.get(position)
        }

    }
}