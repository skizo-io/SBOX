package com.smackjeeves.model

import com.smackjeeves.R
import com.smackjeeves.model.base.BaseResponse
import com.smackjeeves.model.base.Result
import skizo.library.extensions.formatDate
import skizo.library.extensions.getStringFromRes
import java.util.*

data class ItemBox(override var result: Result, var data: ItemBoxData) : BaseResponse()

data class ItemBoxData(var items: List<ItemBoxItem>)

data class ItemBoxItem(
    var unit: String,
    var caption: String,
    var expireTime: Date
) {
    val date: String get() = getStringFromRes(R.string.expires) + " " + expireTime.formatDate
    val icon: Int get() = if(unit.equals("Coin")) R.drawable.icon_coin else R.drawable.icon_ticket
}

