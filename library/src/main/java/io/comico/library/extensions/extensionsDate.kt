package io.comico.library.extensions

import android.content.Context
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


val Date.formatDateTime: String
    get() = SimpleDateFormat.getDateTimeInstance(DateFormat.DEFAULT, SimpleDateFormat.MEDIUM, Locale.US).format(this)

fun Date.formatMonthDate(locale: Locale = Locale.getDefault()): String {
    return SimpleDateFormat("MMM dd", locale).format(this)
}

fun Date.formatDate(locale: Locale = Locale.getDefault()): String {
    return SimpleDateFormat("MMM dd,yyyy", locale).format(this)
}

val Long.formatDate: String
    get() = Date(this).formatDate()

val Int.formatDays: String
    get() = String()


val Date.dDay: String
    get() {
        


        return ""
    }