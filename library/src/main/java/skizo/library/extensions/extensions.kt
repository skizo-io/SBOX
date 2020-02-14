@file:JvmName("util")

package skizo.library.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import skizo.library.Builder
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.security.AccessController.getContext
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
//        var name = this?.apply { getSimpleName } ?: ""
        Log.println(Log.VERBOSE, "trace//", log.joinToString(" : "))
    }
}

fun <T : Any> T?.notNull(notNullFunc: (it: T) -> Unit, nullFunc: (() -> Unit)? = null) {
    if (this == null) nullFunc?.let { nullFunc() } else notNullFunc(this)
}

private val uiHandler = Handler(Looper.getMainLooper())
fun mainThread(runnable: () -> Unit) {
    uiHandler.post(runnable)
}

fun delayed(runnable: () -> Unit, delayMillis: Long = 500) {
    uiHandler.postDelayed(runnable, delayMillis)
}


inline fun <reified T : Activity> Activity.newActivity(vararg pairs: Pair<String, Any?>): Unit =
    this.startActivity(newIntent<T>(*pairs))

inline fun <reified T : Context> Context.newIntent(vararg pairs: Pair<String, Any?>): Intent =
    Intent(this, T::class.java).apply { putExtras(bundleOf(*pairs)) }


fun Any?.showToast(message: Any) {
    when (message) {
        is @StringRes Int -> globalContext?.getString(message)
        is String -> message
        else -> return
    }?.let {
        mainThread {
            Toast.makeText(globalContext, it, Toast.LENGTH_LONG).apply {
                setGravity(Gravity.BOTTOM, 0, 100.toPx)
                show()
            }
        }

    }
}



fun Any?.browse(url: String, newTask: Boolean = true) {
    Intent(Intent.ACTION_VIEW).let {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        it.data = Uri.parse(url)
        globalContext?.startActivity(it)
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


fun <T : View> T.click(block: (T) -> Unit) = setOnClickListener { block(it as T) }
fun <T : View> T.longClick(block: (T) -> Boolean) = setOnLongClickListener { block(it as T) }
fun Any?.setOnClickListeners(listener: (View) -> Unit, vararg views: View) {
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
            requestFeature(Window.FEATURE_NO_TITLE)
        }
        it.setContentView(ProgressBar(globalContext))
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
    var window = dialog.getWindow()
    window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    window.setBackgroundDrawableResource(android.R.color.transparent)
    window.requestFeature(Window.FEATURE_NO_TITLE)
    window.setLayout(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.MATCH_PARENT
    )
//    dialog.setContentView(R.layout.dialog_maintenance)
    dialog.setCancelable(false)
    dialog.show()
}


val Any?.isNotNull: Boolean get() = if (this == null) true else false


fun <T> LiveData<T>.observeNotNull(owner: LifecycleOwner, observer: (T) -> Unit) {
    this.observe(owner, Observer {
        if (it != null) {
            observer(it)
        }
    })
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

