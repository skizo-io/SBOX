package com.smackjeeves.model

import com.smackjeeves.R
import com.smackjeeves.SmackApplication
import com.smackjeeves.model.base.BaseResponse
import com.smackjeeves.model.base.Result
import com.smackjeeves.model.item.Author
import com.smackjeeves.model.item.InfeedAds
import com.smackjeeves.model.item.Title

data class Titles(override var result: Result, var data: TitleData): BaseResponse()

data class TitleData(
    var weekday: String,
    var author: Author,
    var titles: List<Title>,
    var infeedAds: List<InfeedAds>) {


    fun getTitles(sort: String): List<Title> {
        when(sort) {
            SmackApplication.instance?.getString(R.string.sort_new) -> {
                return titles.sortedByDescending { it.releaseTime.time }
            }
            SmackApplication.instance?.getString(R.string.sort_popula) -> {
                return titles.sortedByDescending { it.supportCount }
            }
            else -> {
                return titles
            }
        }
    }


}