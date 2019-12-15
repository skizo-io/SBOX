package com.smackjeeves.model.item

import android.content.Context
import com.smackjeeves.R
import com.smackjeeves.ui.viewer.ViewerActivity
import com.smackjeeves.utils.*
import skizo.library.extensions.formatDate
import skizo.library.extensions.formatDateTime
import skizo.library.extensions.formatSupportCount
import skizo.library.extensions.getStringFromRes
import java.util.*


data class Article(
    var id: Int,
    var order: Int,
    var name: String,
    var thmUrl: String,
    var supportCount: Int,
    var releaseTime: Date,
    var product: Product,
    var activity: Activity
) {

    var enableCheck: Boolean = false
    var isCheck: Boolean = false


    val date: String get() = releaseTime.formatDate
    val supportCountString: String get() = supportCount.formatSupportCount

    fun onClick(context: Context, titleId: Int) {
        context.newActivity<ViewerActivity>("titleId".to(titleId), "articleId".to(id))
    }

    val messagePurchase: String get() = getStringFromRes(R.string.purchase_with, product?.price)
    val messageStatus: String
        get() {
            activity?.takeIf { it.isPurchased }
                ?.apply { return getStringFromRes(R.string.purchase) }
            product?.type?.takeIf { it.isFree }?.apply { return getStringFromRes(R.string.free) }
            activity?.rentalExpireTime?.let {
                return getStringFromRes(R.string.expires) + " " + activity.rentalExpireTime.formatDateTime
            }
            if (product == null) return return getStringFromRes(R.string.free)

            return ""
        }

    val visibleRent: Boolean
        get() {
            if (messageStatus.isNotEmpty()) return false
            product?.type?.takeUnless { it.canUseTicket }?.let { return false }
            return true
        }
    val visiblePurchase: Boolean get() = messageStatus.isNullOrEmpty()
    val visibleStatus: Boolean get() = messageStatus.isNotEmpty()


    val enableDownload: Boolean
        get() {
            activity?.takeIf { it.isPurchased }?.apply {
                product?.type?.takeIf { it.isFree }?.apply {
                    return true
                }
            }
            return false
        }
    val enablePurchase: Boolean
        get() {
            activity?.takeIf { it.isPurchased }?.apply {
                product?.type?.takeIf { it.isFree }?.apply {
                    return true
                }
            }
            return false
        }


}

data class Product(
    var id: Int,
    var price: Int,
    var rentalTerm: Int,
    var isPreview: Boolean,
    var type: Type
)

data class Type(
    var isFree: Boolean,
    var canUseTicket: Boolean
)


data class Activity(
    var isRead: Boolean,
    var isSupported: Boolean,
    var isPurchased: Boolean,
    var purchasedTime: Date,
    var rentalExpireTime: Date
)

