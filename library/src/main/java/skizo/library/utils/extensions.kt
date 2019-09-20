package skizo.library.utils

import android.util.Log
import skizo.library.BuildConfig
import kotlin.reflect.KClass


val isDebugMode: Boolean by lazy {
    BuildConfig.DEBUG_MODE
}


fun <T : Any> T.getClass(): KClass<out T> = this::class
val <T : Any> T.getSimpleName: String get() = getClass().java.simpleName
val <T : Any> T.kClass: KClass<T> get() = javaClass.kotlin

fun todo(message: String = "") {
    if (!isDebugMode) throw NullPointerException("###### TEST CODE : $message ######")
}

fun trace(vararg log: Any?) {
    if (isDebugMode) {
//        var name = this?.apply { getSimpleName } ?: ""
        Log.println(Log.VERBOSE, "comico//", log.joinToString(" : "))
    }
}