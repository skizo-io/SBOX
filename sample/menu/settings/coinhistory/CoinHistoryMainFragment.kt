package com.smackjeeves.ui.menu.settings.coinhistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.smackjeeves.R
import com.smackjeeves.ui.base.BaseFragment
import skizo.library.extensions.visible
import kotlinx.android.synthetic.main.common_viewpager.*
import kotlinx.android.synthetic.main.item_appbar.*


class CoinHistoryMainFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle? = null) =
            CoinHistoryMainFragment().apply {
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

        appbar_title.setText(R.string.coin_history)
        appbar.setVisible(back = true)

        viewpager.offscreenPageLimit = 2
        viewpager.adapter = HistoryPageAdapter(childFragmentManager, resources.getStringArray(R.array.coin_history_tab_categories))
        viewpager.adapter?.notifyDataSetChanged()

        appbar_tabbar.visible = true
        appbar_tabbar.tabMode = TabLayout.MODE_FIXED
        appbar_tabbar.setupWithViewPager(viewpager)
    }


    class HistoryPageAdapter(fm: FragmentManager, var categories: Array<String>) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return CoinHistoryListFragment.newInstance(if(position == 1) false else true)
        }

        override fun getCount(): Int {
            return categories.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return categories.get(position)
        }

    }
}