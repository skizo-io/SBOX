package com.smackjeeves.model

import com.google.gson.annotations.SerializedName
import com.smackjeeves.model.base.BaseResponse
import com.smackjeeves.model.base.Result
import skizo.library.extensions.formatDate
import java.util.*

data class CoinHistory(override var result: Result, var data: CoinHistoryData) : BaseResponse()

data class CoinHistoryData(var purchase: List<CoinHistoryItem>, var usage: List<CoinHistoryItem>)

data class CoinHistoryItem(
    var caption: String,
    var description: String,
    @SerializedName("date") var useTime: Date,
    var price: String
) {
    val date: String get() = useTime.formatDate
    val visibleDescription: Boolean
        get() {
            description?.let {
                return description.isNotEmpty()
            }
            return false
        }
}

