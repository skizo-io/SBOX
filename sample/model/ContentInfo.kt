package com.smackjeeves.model

import android.annotation.SuppressLint
import com.smackjeeves.R
import com.smackjeeves.model.base.BaseResponse
import com.smackjeeves.model.base.Result
import com.smackjeeves.model.item.*
import com.smackjeeves.preferences.UserPreference
import skizo.library.extensions.getQuantityStringFromRes
import skizo.library.extensions.getStringFromRes
import skizo.library.extensions.trace

data class ContentInfo(override var result: Result, var data: ContentInfoData) : BaseResponse()

data class ContentInfoData(
    var title: Title,
    var author: Author,
    var share: Share,
    var article: Article,
    var control: Control,
    var contents: Contents,
    var recommends: List<Recommends>
) {

    var enableContent: Boolean = false
        get() {
            contents?.imgs?.takeIf {
                it.size > 0
            }?.let {
                if(article.activity.isPurchased || article.product.type.isFree) {
                    return true
                }
            }
            return false
        }


    var viewUserCoin: String = "0"
        get() = UserPreference.balanceCoin?.value?.toString()?:"0"
    var viewUserEventTicket: String = "0"
        get() = title.activity.countEventTicket.toString()
    var viewUserDailyTicket: String = "0"
        get() = UserPreference.balanceTicket?.daily?.toString()?:"0"


    var viewBtnCoin: String = ""
        get() = getStringFromRes(R.string.paywall_btn_coin, article.product.price)
    var viewBtnTicket: String = ""
        @SuppressLint("ResourceType")
        get() {
            var value = ""

            trace("@@@@@@@@@@@@@@@@@@@@ viewBtnTicket", value, article.product.rentalTerm)
            value = getQuantityStringFromRes(R.plurals.paywall_btn_ticket, article.product.rentalTerm)
            trace("@@@@@@@@@@@@@@@@@@@@ viewBtnTicket", value)

            return value
        }





}


data class Control(
    var nextArticleId: Int?,
    var prevArticleId: Int?
)

data class Contents(
    var isOneTime: Boolean,
    var direction: String,
    var imgs: List<Image>
) {
    val isDirectionUTD: Boolean
        get() = direction.contains("UDT")
}

data class Image(var w: Int, var h: Int, var url: String)

data class Recommends(var caption: String,
                     var titles: List<Title>)