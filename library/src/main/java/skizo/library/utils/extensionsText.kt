package skizo.library.utils

import android.graphics.Paint
import android.graphics.Typeface
import android.net.Uri
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.*
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat


/**
 * SpannableStringBuilder Text Util
 */
enum class TextType {
    BOLD, UNDERLINE, ITALIC, BOLD_UNDERLINE
}
private fun SpannableStringBuilder.setSpan(view: TextView, start: Int, end: Int,
                                           type: TextType? = null, @ColorRes color: Int? = null, size: Int? = null, block: (() -> Unit)? = null) {
    val isUnderline = type == TextType.UNDERLINE || type == TextType.BOLD_UNDERLINE
    val isBold = type == TextType.BOLD || type == TextType.BOLD_UNDERLINE
    val isItalic = type == TextType.ITALIC

    color?.let {
        setSpan(ForegroundColorSpan(ContextCompat.getColor(view.context, color)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    size?.let {
        setSpan(AbsoluteSizeSpan(size, true), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    block?.let {
        setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                block()
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = isUnderline
            }
        }, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        view.setMovementMethod(LinkMovementMethod.getInstance())
    }

    if(isUnderline)
        setSpan(UnderlineSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    if(isBold)
        setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    if(isItalic)
        setSpan(StyleSpan(Typeface.ITALIC), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
}

fun TextView.add(str: String,
                 type: TextType? = null, @ColorRes color: Int? = null, size: Int? = null, block: (() -> Unit)? = null) {
    SpannableStringBuilder(text)?.let {
        val beforeLength = it.length
        it.append(str)
        it.setSpan(this, beforeLength, it.length, type, color, size, block)
        setText(it, TextView.BufferType.SPANNABLE)
    }
}

fun TextView.replace(str: String,
                     type: TextType? = null, @ColorRes color: Int? = null, size: Int? = null, block: (() -> Unit)? = null) {
    SpannableStringBuilder(text)?.let {
        val startIndex = text.indexOf(str)
        val endIndex = if(startIndex >= 0) startIndex + str.length else -1
        if(startIndex >= 0) {
            it.setSpan(this, startIndex, endIndex, type, color, size, block)
            setText(it, TextView.BufferType.SPANNABLE)
        }
    }
}
/**
 * end
 */



/**
 * usage
 * android:scheme="schemeName"
 *
 * fun Context.openScheme(scheme: String) {
 *  var uri = Uri.parse(scheme)
 *  if( TextUtils.equals(uri.scheme, "schemeName") ) {
 *    schemeParser(uri, "app/main/{id}/sample")?.apply {
 *      return
 *    }
 *  }
 * }
 */
private fun schemeParser(uri: Uri, format: String): HashMap<String, Any>? {
    var path = uri.host + uri.path

    var pathList = path.split("/")
    var formatList = format.split("/")

    if(pathList.size == formatList.size) {

        var params: HashMap<String, Any> = HashMap()
        var cnt = pathList.size

        for (i in 0 until cnt) {
            var f = formatList.get(i)
            var p = pathList.get(i)
            if(f.equals(p)) {
            } else if(Regex("""^\{.*\}$""").matches(f)) {
                params.put(f.replace("""^\{|\}$""".toRegex(), ""), p)
            } else return null
        }

        var paramNames = uri.queryParameterNames
        paramNames.forEach {
            params.put(it, uri.getQueryParameter(it))
        }

        trace("### scheme matches : ", path, params)

        return params
    }
    return null
}
/**
 * end
 */


fun TextView.bold() {
    paint.isFakeBoldText = true
    paint.isAntiAlias = true
}
fun TextView.deleteLine() {
    paint.flags = paint.flags or Paint.STRIKE_THRU_TEXT_FLAG
    paint.isAntiAlias = true
}
fun TextView.underLine() {
    paint.flags = paint.flags or Paint.UNDERLINE_TEXT_FLAG
    paint.isAntiAlias = true
}
fun TextView.setColorOfSubstring(substring: String, @ColorRes color: Int) {
    try {
        val spannable = android.text.SpannableString(text)
        val start = text.indexOf(substring)
        spannable.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, color)), start, start + substring.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        text = spannable
    } catch (e: Exception) {
        trace("ViewExtensions",  "exception in setColorOfSubstring, text=$text, substring=$substring", e)
    }
}


fun String?.isNotEmptyFunc(func: (String)->Unit) {
    if(!this.isNullOrEmpty()) func(this)
}

fun Int.twoDigitTime() = if (this < 10) "0" + toString() else toString()

fun String.equalsIgnoreCase(other: String) = this.toLowerCase().contentEquals(other.toLowerCase())


private fun bytes2Hex(bts: ByteArray): String {
    var des = ""
    var tmp: String
    for (i in bts.indices) {
        tmp = Integer.toHexString(bts[i].toInt() and 0xFF)
        if (tmp.length == 1) {
            des += "0"
        }
        des += tmp
    }
    return des
}



