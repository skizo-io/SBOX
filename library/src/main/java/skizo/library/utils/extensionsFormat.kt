package skizo.library.utils

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


//val Int.formatSupportCount: String
//    get() = NumberFormat.getNumberInstance(Locale.US).format(this)

val Date.formatDateTime: String
    get() = SimpleDateFormat("dd/MM/yy HH:mm").format(this)

val Date.formatDate: String
    get() = SimpleDateFormat("dd/MM/yy").format(this)

val Long.formatDate: String
    get() = SimpleDateFormat("dd/MM/yy").format(this)





fun String.isPhone(): Boolean {
    val p = "^1([34578])\\d{9}\$".toRegex()
    return matches(p)
}

fun String.isEmail(): Boolean {
    val p = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)\$".toRegex()
    return matches(p)
}

fun String.isNumeric(): Boolean {
    val p = "^[0-9]+$".toRegex()
    return matches(p)
}