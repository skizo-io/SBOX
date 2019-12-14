package skizo.library.extensions

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


val Int.formatSupportCount: String
    get() = NumberFormat.getNumberInstance(Locale.US).format(this)

val Date.formatDateTime: String
    get() = SimpleDateFormat("dd/MM/yy HH:mm").format(this)

val Date.formatDate: String
    get() = SimpleDateFormat("dd/MM/yy").format(this)

val Long.formatDate: String
    get() = SimpleDateFormat("dd/MM/yy").format(this)

val Int.formatDays: String
    get() = String()