package com.smackjeeves.model.item

import com.google.gson.annotations.SerializedName
import com.smackjeeves.network.Api
import skizo.library.extensions.formatDate
import skizo.library.extensions.formatSupportCount
import skizo.library.extensions.showToast
import skizo.library.extensions.showToast
import java.util.*


data class Comment(
    var id: Int,
    var titleId: Int,
    var articleId: Int,
    var avatarImgUrl: String,
    var nickname: String,
    var message: String,
    @SerializedName("date") var writeTime: Date,
    var goodCount: Int,
    @SerializedName("enableGood") var good: Boolean,
    @SerializedName("enableDelete") var delete: Boolean) {

    val date: String get() = writeTime.formatDate
    val supportCountString: String get() = goodCount.formatSupportCount

    val enableGood: Boolean get() = good
    val enableDelete: Boolean get() = delete


    fun onGood() {
        showToast("@@@@@@@@@@ onGood " + nickname)
        Api.service.goodComment(titleId, articleId, id)
        good = false
        goodCount++
    }

    fun onReport() {
        showToast("comment report : " + nickname)
        Api.service.reportComment(titleId, articleId, id)
    }

    fun onDelete() {
        showToast("comment delete : " + nickname)
        Api.service.deleteComment(titleId, articleId, id)

    }
}