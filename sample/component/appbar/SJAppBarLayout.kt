package com.smackjeeves.ui.component.appbar

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.annotation.ColorRes
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.item_appbar.view.*
import com.smackjeeves.R
import com.smackjeeves.ui.search.SearchActivity
import com.smackjeeves.utils.*
import skizo.library.extensions.getChildFromTag
import skizo.library.extensions.setColor
import skizo.library.extensions.toPx
import skizo.library.extensions.visible


class SJAppBarLayout : AppBarLayout {

    constructor(context: Context) : super(context) {
        initialize(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialize(context, attrs)
    }

    val activity: Activity?

    init {
        activity = if (context is Activity) context as Activity else null
    }

    private fun initialize(context: Context, attrs: AttributeSet?) {
        /*
        attrs?.let {
            val a = context.obtainStyledAttributes(it, R.styleable.SJAppBar)
            if (a.hasValue(R.styleable.SJAppBar_title)) {
                var myString = a.getString(R.styleable.SJAppBar_title)
                appbar_title.text = myString
                a.recycle()
            }
        }
        */
    }


    override fun onViewAdded(child: View?) {
        super.onViewAdded(child)
        appbar_item_back?.setOnClickListener {
            listenerBack?.let { it() }
        }
        appbar_item_search?.setOnClickListener {
            listenerSearch?.let { it() }
        }
        appbar_item_close?.setOnClickListener {
            listenerClose?.let { it() }
        }
        appbar_item_favorite?.setOnClickListener {
            listenerFavorite?.let { it() }
        }
        appbar_item_more?.setOnClickListener {
            listenerMore?.let { it(appbar_item_more) }
        }
        appbar_item_edit?.setOnClickListener {
            listenerEdit?.let { it() }
        }
        setTheme(false)
    }


    // content
    var visibleLogo = false
        set(value) {
            if (value) {
                ImageView(context).apply {
                    setBackgroundResource(R.drawable.smack_jeeves)
                    this@SJAppBarLayout.appbar_content_layout.addView(this)
                    var param = layoutParams
                    param.width = 144.toPx
                    param.height = 16.toPx
                    if (param is RelativeLayout.LayoutParams) {
                        param.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
                    }
                    tag = "logo"
                }
            } else {
                appbar_content_layout?.getChildFromTag("logo")?.let {
                    appbar_content_layout.removeView(it)
                }
            }
        }

    fun setTheme(isBlack: Boolean, isTransparentBack: Boolean = false) {

        @ColorRes var id: Int = if (isBlack) R.color.white else R.color.black
        appbar_item_back?.setColor(id)
        appbar_item_search?.setColor(id)
        appbar_item_close?.setColor(id)
        appbar_item_favorite?.setColor(id)
        appbar_item_more?.setColor(id)
        appbar_title?.setColor(id)

        id = if (isBlack) R.color.black else R.color.white
        if (isTransparentBack) id = R.color.transparent

        appbar?.setBackgroundResource(id)
    }


    fun setVisible(
        back: Boolean = false,
        search: Boolean = false,
        close: Boolean = false,
        favorite: Boolean = false,
        more: Boolean = false,
        edit: Boolean = false
    ) {
        appbar_item_back?.apply { visible = back }
        appbar_item_search?.apply { visible = search }
        appbar_item_close?.apply { visible = close }
        appbar_item_favorite?.apply { visible = favorite }
        appbar_item_more?.apply { visible = more }
        appbar_item_edit?.apply { visible = edit }
    }

    var listenerBack: (() -> Unit)? = { activity?.onBackPressed() }
        set(value) {
            appbar_item_back.visible = true
            field = value
        }
    var listenerSearch: (() -> Unit)? = {
        context.newActivity<SearchActivity>("search".to(""))
    }
        set(value) {
            appbar_item_search.visible = true
            field = value
        }
    var listenerClose: (() -> Unit)? = { activity?.finish() }
        set(value) {
            appbar_item_close.visible = true
            field = value
        }
    var listenerFavorite: (() -> Unit)? = { }
        set(value) {
            appbar_item_favorite?.apply { visible = true }
            field = value
        }
    var listenerMore: ((View) -> Unit)? = { }
        set(value) {
            appbar_item_more?.apply { visible = true }
            field = value
        }
    var listenerEdit: (() -> Unit)? = { }
        set(value) {
            appbar_item_edit?.apply { visible = true }
            field = value
        }


    fun setCustomAppbar(view: View) {
        appbar_custom_layout.removeAllViews()
        appbar_custom_layout.addView(view)
    }

    var visibleTabbar = false
        set(value) {
            appbar_tabbar?.apply { visible = value }
            tabbar_line?.apply { visible = value }
        }


}