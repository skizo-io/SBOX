package com.smackjeeves.model

import android.view.View
import androidx.annotation.StringRes
import com.smackjeeves.R
import com.smackjeeves.model.base.BaseResponse
import com.smackjeeves.model.base.Result
import skizo.library.extensions.getStringFromRes
import com.smackjeeves.utils.openScheme
import java.util.*
import java.util.concurrent.TimeUnit

data class Giftbox(override var result: Result, var data: GiftboxData) : BaseResponse()

data class GiftboxData(var messages: List<GiftboxItem>)

data class GiftboxItem(
    var id: Int,
    var description: String,
    var thmUrl: String,
    var link: String,
    var expireTime: Date,
    var isReceived: Boolean
) {

    private fun createMessage(@StringRes id: Int, time: Long, @StringRes unit: Int): String {
        return "<font color=\"" + getStringFromRes(id) + "\">" +
                getStringFromRes(R.string.gift_expires_in) + " " + time + " " +
                getStringFromRes(unit) + "</font>"
    }

    val expireMessage: String
        get() {

            if(isReceived) return ""

            var remainTime: Long = expireTime.time - Date().time

            if (TimeUnit.DAYS.convert(remainTime, TimeUnit.MILLISECONDS) > 0) {
                return createMessage(
                    R.string.color_dark_grey,
                    TimeUnit.DAYS.convert(remainTime, TimeUnit.MILLISECONDS),
                    R.string.days
                )
            } else if (TimeUnit.HOURS.convert(remainTime, TimeUnit.MILLISECONDS) > 0) {
                return createMessage(
                    R.string.color_error,
                    TimeUnit.HOURS.convert(remainTime, TimeUnit.MILLISECONDS),
                    R.string.hours
                )
            } else if (TimeUnit.MINUTES.convert(remainTime, TimeUnit.MILLISECONDS) > 0) {
                return createMessage(
                    R.string.color_error,
                    TimeUnit.MINUTES.convert(remainTime, TimeUnit.MILLISECONDS),
                    R.string.minutes
                )
            } else if (TimeUnit.SECONDS.convert(remainTime, TimeUnit.MILLISECONDS) > 0) {
                return createMessage(R.string.color_error, 1, R.string.minutes)
            }
            return "<font color=\"" + getStringFromRes(R.string.color_error) + "\">" + getStringFromRes(
                R.string.expires
            ) + "</font>"
        }

    val enableReceive: Boolean
        get() {
            var remainTime: Long = expireTime.time - Date().time
            return remainTime > 0 && !isReceived
        }


    fun onRead(v: View, link: String) {
        v.context.openScheme(link)
    }


}

