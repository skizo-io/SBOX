package com.smackjeeves.event

import androidx.viewpager.widget.ViewPager

open class OnViewPagerListener: InnerOnViewPagerListener() {
    override fun onViewPagerChanged() {
        super.onViewPagerChanged()
    }

    override fun onViewPagerScrolled(position: Int, positionOffset: Float) {
        super.onViewPagerScrolled(position, positionOffset)
    }

    override fun onViewPagerSelected(position: Int) {
        super.onViewPagerSelected(position)
    }
}

open class InnerOnViewPagerListener: ViewPager.OnPageChangeListener {
    var currentPosition = -1
    var isStateSeleted = true

    override fun onPageScrollStateChanged(state: Int) {

        when (state) {
            ViewPager.SCROLL_STATE_IDLE -> isStateSeleted = true
            else -> {
                if(isStateSeleted) {
                    isStateSeleted = false
                    onViewPagerChanged()
                }
            }
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        viewPagerListener(position, positionOffset)
    }

    override fun onPageSelected(position: Int) {
        viewPagerListener(position, 0f)
    }


    fun viewPagerListener(position: Int, positionOffset: Float) {
        if(positionOffset > 0) {
            onViewPagerScrolled(position, positionOffset)
        } else if(currentPosition != position || !isStateSeleted) {
            onViewPagerSelected(position)
            currentPosition = position
            isStateSeleted = true
        }
    }

    open fun onViewPagerChanged() {}
    open fun onViewPagerScrolled(position: Int, positionOffset: Float) {}
    open fun onViewPagerSelected(position: Int) {}
}
