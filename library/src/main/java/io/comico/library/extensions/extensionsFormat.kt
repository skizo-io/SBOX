package io.comico.library.extensions

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


val Int.formatSupportCount: String
    get() = NumberFormat.getNumberInstance(Locale.US).format(this)
