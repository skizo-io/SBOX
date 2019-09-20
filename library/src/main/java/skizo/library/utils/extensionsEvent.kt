@file:JvmName("EventReceiver")

package skizo.library.utils

import java.util.*
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter


data class ReceiverItem(val type: String, var any: Any? = null)
interface IEventReceiver {
    fun onEventReceiver(item: ReceiverItem)
}

private var map: HashMap<String, ArrayList<KFunction<*>>> = HashMap()
private fun addReceiver(type: String, listener: Any) {
    listener.takeIf { listener is KFunction<*> }?.let {
        val list: ArrayList<KFunction<*>>? = map.get(type) ?: ArrayList()
        list?.let {
            if (listener !in it)
                it.add(listener as KFunction<*>)
            map.put(type, it)
        }
    }
}





fun addEventReceiver(type: String, listener: IEventReceiver) = addReceiver(type, listener::onEventReceiver)
fun addEventReceiver(type: String, listener: () -> Unit) = addReceiver(type, listener)
fun addEventReceiver(type: String, listener: (ReceiverItem) -> Unit) = addReceiver(type, listener)

fun removeEventReceiver(listener: Any?) = removeEventReceiver(null, listener)
fun removeEventReceiver(type: String? = null, listener: Any? = null) {
    var kfun: KFunction<*>? = null
    if (listener is IEventReceiver) kfun = listener::onEventReceiver
    else if (listener is KFunction<*>) kfun = listener

    if (type == null) {
        for ((T, F) in map) {
            if (kfun == null) F.clear()
            else F.remove(kfun)
            map.put(T, F)
        }
    } else {
        map.get(type)?.apply {
            if (kfun == null) clear()
            else remove(kfun)
            map.put(type, this)
        }
    }
}

fun dispatcher(type: String, item: Any? = null) {
    map.get(type)?.let {
        for (func in it) {
            val parameters = func.parameters
            HashMap<KParameter, ReceiverItem>(parameters.size).let {
                if (parameters.size == 1) {
                    it.put(parameters.get(0), ReceiverItem(type, item))
                }
                func.callBy(it)
            }
        }
    }

}
