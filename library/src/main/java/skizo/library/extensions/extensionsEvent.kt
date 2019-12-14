@file:JvmName("EventReceiver")

package skizo.library.extensions

import android.annotation.SuppressLint
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.jvm.javaType


data class ReceiverItem<T>(val type: Any, var item: T?)

private var map: HashMap<Any, ArrayList<KFunction<*>>> = HashMap()

private fun addReceiver(type: Any, listener: Any) {

    if (listener is KFunction<*>) {
        val list = map.get(type) ?: ArrayList()

        list?.let {
            if (listener !in it) it.add(listener)
            map.put(type, it)
        }
    }
}


fun addEventReceiver(type: Any, listener: () -> Unit) =
    addReceiver(type, listener)
fun <T> addEventReceiver(type: Any, listener: (T) -> Unit) =
    addReceiver(type, listener)

@SuppressLint("NewApi")
fun removeEventReceiver(type: Any? = null, listener: Any? = null) {

    fun removeListener(T: Any, F: ArrayList<KFunction<*>>) {
        if (listener == null) F.clear()
        else if (listener is KFunction<*>) F.remove(listener)
        else {
            F.removeIf {
                val p = ("fun |." + it.name + "(.*)").toRegex()
                val fName = p.replace(it.toString(), "")
                val lName = listener.javaClass.canonicalName
                fName.equals(lName)
            }
        }
        map.put(T, F)
    }

    if (type == null) {
        for ((T, F) in map) {
            removeListener(T, F)
        }
    } else {
        map.get(type)?.apply {
            removeListener(type, this)
        }
    }
}

fun dispatcherEvent(type: Any) =
    dispatcherEvent(type, null)
@SuppressLint("NewApi")
fun <T : Any> dispatcherEvent(type: Any, item: T?) {

    map.get(type)?.let {
        for (func in it) {
            val parameters = func.parameters
            when (parameters.size) {
                1 -> {
                    if (parameters.get(0).type.classifier == ReceiverItem::class && parameters.get(0).type.arguments.size == 1) {
                        HashMap<KParameter, ReceiverItem<T>>(parameters.size).let {
                            if (parameters.get(0).type.arguments.get(0).type?.javaType == item?.javaClass || item == null) {
                                it.put(parameters.get(0),
                                    ReceiverItem(
                                        type,
                                        item
                                    )
                                )
                                func.callBy(it)
                            }
                        }
                    } else if (parameters.get(0).type.classifier == item?.javaClass?.kotlin || item == null) {
                        try {
                            if (parameters.get(0).isOptional && item == null) {
                                func.callBy(mapOf())
                            } else {
                                HashMap<KParameter, T?>(parameters.size).let {
                                    it.put(parameters.get(0), item)
                                    func.callBy(it)
                                }
                            }
                        } catch (e: Exception) {

                        }
                    }
                }
                else -> {
                    func.call()
                }
            }
        }
    }
}

