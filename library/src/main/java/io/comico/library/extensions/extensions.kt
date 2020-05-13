@file:JvmName("util")

package io.comico.library.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.*
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import io.comico.library.Builder
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import kotlin.reflect.KClass
import kotlin.reflect.KProperty


fun <T : Any> T.getClass(): KClass<out T> = this::class
val <T : Any> T.getSimpleName: String get() = getClass().java.simpleName
val <T : Any> T.kClass: KClass<T> get() = javaClass.kotlin

val Any?.globalContext: Context?
    get() {
        return takeIf { this is Context }?.let { it as Context } ?: Builder.context
    }

fun todo(message: String = "") {
    if (!Builder.isDebugMode) throw NullPointerException("###### TEST CODE : $message ######")
}

fun trace(vararg log: Any?) {
    if (Builder.isDebugMode) {
        val className = Throwable().stackTrace[1].className.split(".").last()
        val tag = "trace://$className"

        var logText = log.joinToString(" : ")
//        Log.println(Log.VERBOSE, tag, logText)

        val max = 4000
        while (logText.length > 0) {
            if (logText.length > max) {
                Log.println(Log.VERBOSE, tag, logText.substring(0, max))
                logText = logText.substring(max)
            } else {
                Log.println(Log.VERBOSE, tag, logText)
                logText = ""
            }
        }


        /*
        val max = 3000
        var temp = ""
        var list = arrayListOf<String>()

        log.forEachIndexed { index, it ->
            var str = it.toString()

            if(str.length > max) {
                if(temp.isNotEmpty()) {
                    list.add(temp)
                    temp = ""
                }

                while(str.length > 0) {
                    if(str.length > max) {
                        list.add(str.substring(0, max))
                        str = str.substring(max)
                    } else {
                        list.add(str)
                        str = ""
                    }
                }
            } else {
                var value = str
                if(temp.isNotEmpty()) {
                    value = " :  $value"
                }

                if((temp + value).length > max) {
                    list.add(temp)
                    temp = ""
                }
                temp += value
            }
        }
        if(temp.isNotEmpty()) {
            list.add(temp)
            temp = ""
        }

        list.forEachIndexed { index, s ->
            if(index > 0)
                Log.println(Log.VERBOSE, "$tag\n", s)
            else
                Log.println(Log.VERBOSE, tag, s)
        }
        */
    }
}

inline fun Int.forEach(startIndex: Int = 0, action: (Int) -> Unit) {
    for (i in startIndex until this) {
        action(i)
    }
}

fun <T> Boolean.get(`true`: T, `false`: T): T {
    return if (this) `true` else `false`
}

fun <T : Any> T?.nonNull(
    nonNullAction: ((it: T) -> Unit)? = null,
    nullAction: (() -> Unit)? = null
) {
    this?.let {
        nonNullAction?.let {
            it.invoke(this)
        }
        return
    }
    nullAction?.let {
        it.invoke()
    }
}

fun <T : Any> T?.nullInit(value: T): T {
    this?.let {
        return it
    }
    return value
}

val Any?.isNotNull: Boolean get() = if (this == null) false else true

fun <T> LiveData<T>.observeNotNull(owner: LifecycleOwner, observer: (T) -> Unit) {
    this.observe(owner, Observer {
        if (it != null) {
            observer(it)
        }
    })
}


private val uiHandler = Handler(Looper.getMainLooper())
fun mainThread(runnable: () -> Unit) {
    uiHandler.post(runnable)
}

private val handler = Handler()
fun subThread(runnable: () -> Unit) {
    handler.post(runnable)
}


fun delayed(runnable: () -> Unit, delayMillis: Long = 500) {
    uiHandler.postDelayed(runnable, delayMillis)
}

//inline fun <reified T : Context> Context.newIntent(extras: Bundle): Intent = newIntent<T>(0, extras)
inline fun <reified T : Activity> Fragment.newActivity(vararg pairs: Pair<String, Any?>) =
    activity?.apply { newActivity<T>(*pairs) }

inline fun <reified T : Activity> Context.newActivity(vararg pairs: Pair<String, Any?>): Unit =
    this.startActivity(newIntent<T>(*pairs))

inline fun <reified T : Activity> Context.newActivityClearTop(vararg pairs: Pair<String, Any?>): Unit =
    this.startActivity(newIntent<T>(FLAG_ACTIVITY_CLEAR_TOP, *pairs))

inline fun <reified T : Activity> Activity.newActivity(vararg pairs: Pair<String, Any?>): Unit =
    this.startActivity(newIntent<T>(*pairs))

// TODO https://nhn-playart.dooray.com/project/posts/2727558208982970525 対応
// TODO ComicViewerActivityの場合に、FLAG_ACTIVITY_CLEAR_TOPを使いたい
inline fun <reified T : Activity> Activity.newActivityForResult(
    requestCode: Int = 0,
    vararg pairs: Pair<String, Any?>
) =
    this.startActivityForResult(newIntent<T>(FLAG_ACTIVITY_CLEAR_TOP, *pairs), requestCode)

inline fun <reified T : Context> Context.newIntent(vararg pairs: Pair<String, Any?>): Intent =
    Intent(this, T::class.java).apply {
        putExtras(bundleOf(*pairs))
    }

// TODO https://nhn-playart.dooray.com/project/posts/2727558208982970525 対応
inline fun <reified T : Context> Context.newIntent(
    flag: Int,
    vararg pairs: Pair<String, Any?>
): Intent =
    Intent(this, T::class.java).apply {
        putExtras(bundleOf(*pairs))
        flags = flag
    }

//inline fun bundleOf(vararg pairs: Pair<String, Any?>) = Bundle(pairs.size).apply { put(*pairs) }

private var tempToast: Toast? = null
fun Any.showToast(message: Any) {

    var context: Context? = null
    if (this.isNotNull && this is Context) {
        context = this
    } else {
        context = globalContext
    }

    when (message) {
        is @StringRes Int -> globalContext?.getString(message)
        is String -> message
        else -> return
    }?.let {
        context?.apply {
            val layout = RelativeLayout(context)
            layout.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            val card = CardView(context).apply {
                radius = 8.toPx.toFloat(); alpha = 0.8f
                setCardBackgroundColor(android.R.color.black.toColorFromRes)
                val param = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                layout.addView(this, param)
            }
            TextView(context).apply {
                text = it
                gravity = Gravity.CENTER
                setTextColor(android.R.color.white.toColorFromRes)
                val param = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
                param.setMargins(16.toPx)
                card.addView(this, param)
            }

            mainThread {
                tempToast?.apply {
                    cancel()
                }
                tempToast = Toast(context).apply {
                    duration = Toast.LENGTH_SHORT
                    setGravity(Gravity.CENTER, 0, -100.toPx)
                    view = layout
                    show()
                }
            }
        }


    }
}


fun Context.browse(url: String, newTask: Boolean = true) {
    Intent(Intent.ACTION_VIEW).let {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        it.data = Uri.parse(url)
        startActivity(it)
    }
}


val Int.toPx: Int get() = toPx()

@JvmOverloads
//infix fun Int.toPx(context: Context?): Int {
fun Int.toPx(context: Context? = null): Int {
    val ctx = context ?: globalContext
    ctx?.let {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            toFloat(),
            ctx.resources.displayMetrics
        ).toInt()
    }
    return this
}

fun Context.getDimensionPixelSize(@DimenRes resId: Int): Int {
    return resources.getDimensionPixelSize(resId)
}

@ColorInt
fun Any?.getColorFromRes(@ColorRes resId: Int): Int =
    globalContext?.let { ContextCompat.getColor(it, resId) } ?: 0

val Int.toColorFromRes: Int
    get() = getColorFromRes(this)

fun Any?.getStringFromRes(@StringRes stringResId: Int, vararg formatArgs: Any): String =
    globalContext?.resources?.getString(stringResId, *formatArgs) ?: ""

@SuppressLint("ResourceType")
fun Any?.getQuantityStringFromRes(@StringRes stringResId: Int, quantity: Int): String =
    globalContext?.resources?.getQuantityString(stringResId, quantity) ?: ""


// fromApi(23){ パーミッションの処理などに使える }
private val version: Int
    get() = Build.VERSION.SDK_INT

fun fromApi(
    fromVersion: Int,
    inclusive: Boolean = true,
    action: () -> Unit,
    elseAction: (() -> Unit)? = null
) {
    if (version > fromVersion || (inclusive && version == fromVersion)) action()
    else elseAction?.let { it() }
}


val Context.windowInfo: WindowInfo by GetWindowInfo()

data class WindowInfo(var width: Int = 0, val height: Int = 0)
private class GetWindowInfo {
    operator fun getValue(context: Context, property: KProperty<*>): WindowInfo {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val point = Point()
            display.getSize(point)
            return WindowInfo(point.x, point.y)
        }
        return WindowInfo(display.width, display.height)
    }
}

inline val Context.displayWidth: Int
    get() = resources.displayMetrics.widthPixels
inline val Context.displayHeight: Int
    get() = resources.displayMetrics.heightPixels


//fun View.observableState

private var safeClickTime: Long = 0
fun <T : View> T.safeClick(block: (T) -> Unit) = safeClick(block, 300)
fun <T : View> T.safeClick(block: (T) -> Unit, interval: Int = 300) = setOnClickListener {
    val currentClickTime = SystemClock.elapsedRealtime()
    if (currentClickTime > safeClickTime) {
        safeClickTime = currentClickTime + interval
        block(it as T)
    }
}

fun <T : View> T.click(block: (T) -> Unit) = setOnClickListener { block(it as T) }
fun <T : View> T.longClick(block: (T) -> Boolean) = setOnLongClickListener { block(it as T) }
fun Any?.clicks(listener: (View) -> Unit, vararg views: View) {
    views.forEach { it?.click { listener(it) } }
}


private var loadingDialog: Dialog? = null;
fun Context.showLoading() {
    if (loadingDialog == null)
        loadingDialog = Dialog(this)
    loadingDialog?.let {
        it.window?.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            setBackgroundDrawableResource(android.R.color.transparent)
//            requestFeature(Window.FEATURE_NO_TITLE)
        }

        it.setContentView(ProgressBar(it.context))
        it.setCancelable(false)
        it.show()
    }
}

fun Any?.hideLoading() {
    loadingDialog?.let {
        it.hide()
        it.dismiss()
    }
}


fun Context.showMaintenance() {
    var dialog = Dialog(this)
    dialog.getWindow()?.let {
        it.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        it.setBackgroundDrawableResource(android.R.color.transparent)
        it.requestFeature(Window.FEATURE_NO_TITLE)
        it.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
    }
//    dialog.setContentView(R.layout.dialog_maintenance)
    dialog.setCancelable(false)
    dialog.show()
}


tailrec fun Any.destroy(vararg objs: Any? = emptyArray()) {
    try {
        trace(
            "destroy.<<${getSimpleName}>> start ------------------------",
            objs.size
        )

        if (objs.size == 0) destroy(this)
        else {
            for (obj in objs) {
                obj?.let {
                    trace("destroy.<<${getSimpleName}.${obj.getSimpleName}>>")

                    val cls = obj.javaClass
                    val fieldlist = cls.declaredFields
                    for (fld in fieldlist) {
                        val access = Modifier.toString(fld.modifiers)
                        if ("final" !in access && "static" !in access) {
                            val accessible = fld.isAccessible()
                            fld.isAccessible = true
                            val name = fld.name
                            val type = fld.type
                            val variable = fld.get(obj)
                            trace(
                                "destroy.<<${getSimpleName}.${obj.getSimpleName}.field>>",
                                access,
                                name,
                                type,
                                fld.get(obj)
                            )

                            try {
                                fld.set(obj, null)
                                fld.isAccessible = accessible
                            } catch (e: Exception) {
                            }
                            // remove array, map
                        }
                    }


//                    if( it is EventManager.IEventListener)
//                        EventManager.instance.removeEventListener(it)

                    when (it) {
                        is ImageView -> {
                            trace("destroy.<<${getSimpleName}.${obj.getSimpleName}.ImageView>>")

                            it.apply {
                                takeIf { it is BitmapDrawable }?.let {
                                    it.drawable?.apply {
                                        callback = null
                                    }
                                }
                                setBackgroundResource(android.R.color.transparent)
                                setImageBitmap(null)
                            }
                        }
                        is Bitmap -> {
                            trace("destroy.<<${getSimpleName}.${obj.getSimpleName}.Bitmap>>")

                            it.takeUnless { it.isRecycled }?.recycle()
                        }
                        is ViewGroup -> {

                            val paramTypes = arrayOf<Class<*>>(Integer.TYPE)
                            var getChildAt: Method? = cls.getMethod("getChildAt", *paramTypes)
                            var getChildCount: Method? = cls.getMethod("getChildCount")

                            trace(
                                "destroy.<<${getSimpleName}.${obj.getSimpleName}.ViewGroup>>",
                                getChildCount
                            )

                            getChildCount?.let {
                                val cnt = it.invoke(obj) as Int
                                getChildAt?.let {
                                    for (i in 0 until cnt) {
                                        val parameters = arrayOf<Any>(i)
                                        val o = it.invoke(obj, *parameters)
                                        trace(
                                            "destroy.<<${getSimpleName}.${obj.getSimpleName}.ViewGroup>> ",
                                            obj != o,
                                            obj,
                                            o
                                        )

                                        if (obj != o) destroy(o)
                                    }
                                }
                            }

                            cls.getMethod("removeAllViews")?.let {
                                it.invoke(obj)
                            }
                        }
                        else -> {
                            trace("destroy.<<${getSimpleName}.${obj.getSimpleName}.none>>")

                        }

                    }
                }
            }
        }
    } catch (e: Exception) {
    }
}

