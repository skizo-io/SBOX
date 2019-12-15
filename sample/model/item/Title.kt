package com.smackjeeves.model.item

import android.content.Context
import com.smackjeeves.ui.article.ComicTitleActivity
import skizo.library.extensions.formatDate
import skizo.library.extensions.formatSupportCount
import com.smackjeeves.utils.newActivity
import java.util.*

data class Title(var id: Int,
                 var name: String,
                 var author: String,
                 var description: String,
                 var thmUrl: String,
                 var releaseTime: Date,
                 var lastUpdateTime: Date,
                 var supportCount: Int,
                 var genre: String,
                 var genreId: Int,
                 var statut: String,
                 var hiatus: Boolean,
                 var coverImgUrl: String,
                 var coverColor: String,
                 var isDiscover: Boolean,
                 var updateCycle: String,
                 var tags: List<Tag>,
                 var ticketCountUpCycle: Long,
                 var activity: ActivityTitle) {

    val date: String get() = releaseTime.formatDate
    val supportCountString: String get() = supportCount.formatSupportCount

    fun onClick(context: Context) {


        context.newActivity<ComicTitleActivity>("id".to(id), "isDiscover".to(isDiscover))
    }



}


data class ActivityTitle(
    var isFavorite: Boolean,
    var lastReadArticleId: Int,
    var countEventTicket: Int
)
