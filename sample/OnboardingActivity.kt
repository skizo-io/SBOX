package com.smackjeeves.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import com.smackjeeves.R
import com.smackjeeves.ui.base.BaseActivity
import com.smackjeeves.event.OnViewPagerListener
import skizo.library.extensions.toPx
import kotlinx.android.synthetic.main.activity_onboarding.*
import kotlinx.android.synthetic.main.item_appbar.*

class OnboardingActivity : BaseActivity() {

    lateinit var titles: Array<String>
    lateinit var descriptionTop: Array<String>
    lateinit var descriptionBottom: Array<String>
    lateinit var layouts: List<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        titles = resources.getStringArray(R.array.onboarding_title)
        descriptionTop = resources.getStringArray(R.array.onboarding_des_top)
        descriptionBottom = resources.getStringArray(R.array.onboarding_des_bottom)
        layouts = listOf(
            R.layout.item_onboarding_0_welcome,
            R.layout.item_onboarding_1_intro,
            R.layout.item_onboarding_2_horizonatal,
            R.layout.item_onboarding_3_vertical,
            R.layout.item_onboarding_4_offline,
            R.layout.item_onboarding_5_download)

        val w = 12.toPx
        val cnt = layouts.size - 1
        for (i in 0 until cnt) {
            var view = View(applicationContext)
            var params = LinearLayout.LayoutParams(w, w)
            params.rightMargin = 4.toPx
            view.layoutParams = params
            view.setBackgroundResource(R.drawable.shape_circle_white)
            view.tag = i + 1
            onboarding_circleindicator_layout.addView(view)
        }

        onboarding_viewpager?.apply {
            addOnPageChangeListener(object : OnViewPagerListener() {

                override fun onViewPagerChanged() {
                    onboarding_description_top.text = ""
                    onboarding_description_bottom.text = ""
                }

                override fun onViewPagerSelected(position: Int) {
                    viewPagerSelected(position)
                }

            })
            adapter = OnboardingAdpater(layouts)
        }


        appbar.setVisible(back = true, close = true)

    }

    fun viewPagerSelected(position: Int) {
        try {
            appbar_title.text = titles.get(position)
            onboarding_description_top.text = descriptionTop.get(position)
            onboarding_description_bottom.text = descriptionBottom.get(position)
        } catch (e: Exception) {}


        if(position == 0)
            onboarding_circleindicator_layout.visibility = View.INVISIBLE
        else {
            onboarding_circleindicator_layout.visibility = View.VISIBLE

            val cnt = onboarding_circleindicator_layout.childCount
            for (i in 0 until cnt) {
                onboarding_circleindicator_layout.getChildAt(i).apply {
                    setBackgroundResource(
                        if(tag == position)
                            R.drawable.shape_circle_black
                        else
                            R.drawable.shape_circle_white
                    )
                }
            }
        }
    }



    class OnboardingAdpater(var layouts: List<Int>) : PagerAdapter() {

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj
        }

        override fun getCount(): Int {
            return layouts.size
        }

        override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
            container.removeView(obj as View)
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = LayoutInflater.from(container.context).inflate(layouts.get(position), container, false)
            container.addView(view)
            return view
        }


    }


}

