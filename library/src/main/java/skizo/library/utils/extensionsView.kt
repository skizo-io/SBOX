package com.smackjeeves.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment


val View.screenX get() = IntArray(2).apply { getLocationOnScreen(this) }[0]
val View.screenY get() = IntArray(2).apply { getLocationOnScreen(this) }[1]

fun ViewGroup.getChildFromTag(tag: String): View? {
    for (i in 0 until childCount) {
        try {
            if (getChildAt(i).tag.equals(tag)) {
                return getChildAt(i)
            }
        } catch (e: Exception) {
        }
    }
    return null
}

fun ViewGroup.getViewsByTag(tag: String): ArrayList<View> {
    val views = ArrayList<View>()
    val childCount = childCount
    for (i in 0..childCount - 1) {
        val child = getChildAt(i)
        if (child is ViewGroup) {
            views.addAll(child.getViewsByTag(tag))
        }

        val tagObj = child.tag
        if (tagObj != null && tagObj == tag) {
            views.add(child)
        }

    }
    return views
}

fun Fragment.getViewFromResource(@LayoutRes resId: Int): View {
    return LayoutInflater.from(context).inflate(resId, null)
}

fun ViewGroup.removeViewsByTag(tag: String) {
    for (i in 0..childCount - 1) {
        val child = getChildAt(i)
        if (child is ViewGroup) {
            child.removeViewsByTag(tag)
        }

        if (child.tag == tag) {
            removeView(child)
        }
    }
}

fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, 0)
}

fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) {
    }
    return false
}

inline fun <T : View> T.afterMeasured(crossinline f: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                f()
            }
        }
    })
}

fun View.doOnLayout(onLayout: (View) -> Boolean) {
    addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
        override fun onLayoutChange(
            view: View, left: Int, top: Int, right: Int, bottom: Int,
            oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int
        ) {
            if (onLayout(view)) {
                view.removeOnLayoutChangeListener(this)
            }
        }
    })
}

fun View.setPaddingLeft(value: Int) = setPadding(value, paddingTop, paddingRight, paddingBottom)
fun View.setPaddingRight(value: Int) = setPadding(paddingLeft, paddingTop, value, paddingBottom)


fun View.resizeWidth(value: Int): View {
    layoutParams?.let {
        layoutParams.width = value
    }
    if (layoutParams == null) {
        afterMeasured { resizeWidth(value) }
    }
    return this
}

fun View.resizeHeight(value: Int): View {
    layoutParams?.let {
        layoutParams.height = value
    }
    if (layoutParams == null) {
        afterMeasured { resizeHeight(value) }
    }
    return this
}

fun View.resize(width: Int, height: Int): View {
    layoutParams?.let {
        layoutParams.width = width
        layoutParams.height = height
    }
    if (layoutParams == null) {
        afterMeasured { resize(width, height) }
    }
    return this
}


var View.visible
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }


fun View.setColor(@ColorRes id: Int) {
    var color = getColorFromRes(id)
    var filter = PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY)
    when (this) {
        is ImageView -> this.setColorFilter(filter)
        is TextView -> this.setTextColor(color)
        else -> {
        }
    }
}


/*
//https://pluu.github.io/blog/rxjava/2017/02/04/android-alertdialog/
//http://dailyddubby.blogspot.com/2018/04/106-custom-alert-dialog.html
fun Context.openDialog(
    title: String = "",
    message: String = "",
    positive: Pair<String, ()->Unit>? = null,
    negative: Pair<String, ()->Unit>? = null,
    neutral: Pair<String, ()->Unit>? = null
) {
    AlertDialog.Builder(this, R.style.SJAlertDialogStyle).apply {
        title.isNotEmptyFunc { setTitle(it) }
        message.isNotEmptyFunc { setMessage(it) }
        positive?.apply {
            first.isNotEmptyFunc {
                setPositiveButton(it) { dialog, which ->
                    second?.let { it() }
                }
            }
        }
        negative?.apply {
            first.isNotEmptyFunc {
                setNegativeButton(it) { dialog, which ->
                    second?.let { it() }
                }
            }
        }
        neutral?.apply {
            first.isNotEmptyFunc {
                setNeutralButton(it) { _, _ ->
                    second?.let { it() }
                }
            }
        }

        create().show()
    }
}
*/
