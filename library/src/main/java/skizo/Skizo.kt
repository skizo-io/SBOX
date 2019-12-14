package skizo.library

import android.content.Context
import skizo.library.base.BasePreferences

class Skizo {

    companion object {
        fun startInit(context: Context): Builder {
            BasePreferences.init(context)
            return Builder(context)
        }

    }

}

class Builder(val context: Context) {

    companion object {
        var isDebugMode = false
        var context: Context? = null
    }

    init {
        Builder.context = context
    }

    fun isDebugMode(bl: Boolean) {
        isDebugMode = bl
    }

}
