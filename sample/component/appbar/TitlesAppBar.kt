package com.smackjeeves.ui.component.appbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.google.android.material.tabs.TabLayout
import com.smackjeeves.R
import skizo.library.extensions.toPx
import kotlinx.android.synthetic.main.item_titles_appbar.view.*

class TitlesAppBar: RelativeLayout {

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }


    private fun initView() {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_titles_appbar, this)

    }

    fun createTab(menu: Array<String>, callback: (Int)->Unit) {

        titles_tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    selectTab(it.position)
                    callback(it.position)
                }

            }
        })

        menu.forEach {
            titles_tablayout.addTab(titles_tablayout.newTab().setText(it))
        }
        for (i in 0 until titles_tablayout.tabCount) {
            var tab = (titles_tablayout.getChildAt(0) as ViewGroup).getChildAt(i)
            var p = tab.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(if(i == 0) 16.toPx else 2.toPx, 0, 0, 0)
            tab.requestLayout()
        }

    }

    fun selectTab(position: Int) {

    }





}