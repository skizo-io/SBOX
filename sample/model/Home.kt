package com.smackjeeves.model

import com.smackjeeves.model.base.BaseResponse
import com.smackjeeves.model.base.Result
import com.smackjeeves.model.item.Tag
import com.smackjeeves.model.item.Title

data class Home(override var result: Result, var data: HomeData): BaseResponse() {
    val count: Int get() = data.contents.size + 3
}

data class HomeData(
    var rotation: List<RotationCard>,
    var contents: List<ContentCard>,
    var news: List<NewsContent>,
    var tags: List<Tag>)


data class RotationCard(var bannerUrl: String, var scheme: String)
data class ContentCard(var type: String, var caption: String, var titles: List<Title>) {

    val isRankingType: Boolean get() = type.equals("ranking")
}