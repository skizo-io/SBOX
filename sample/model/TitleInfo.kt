package com.smackjeeves.model

import com.smackjeeves.model.base.BaseResponse
import com.smackjeeves.model.base.Result
import com.smackjeeves.model.item.Article
import com.smackjeeves.model.item.Author
import com.smackjeeves.model.item.Share
import com.smackjeeves.model.item.Title

data class TitleInfo(override var result: Result, var data: TitleInfoData): BaseResponse()

data class TitleInfoData(
    var title: Title,
    var author: Author,
    var share: Share,
    var articles: List<Article>) {


    /*
    fun getTitles(sort: String): List<Title> {

        when(sort) {

            getContext().getString(R.string.sort_new) -> {
                return titles.sortedByDescending { it.releaseTime.time }
            }
            getContext().getString(R.string.sort_popula) -> {
                return titles.sortedByDescending { it.supportCount }
            }
            else -> {
                return titles
            }

        }


    }
*/


}