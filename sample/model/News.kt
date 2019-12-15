package com.smackjeeves.model

import com.smackjeeves.model.base.BaseResponse
import com.smackjeeves.model.base.Result
import com.smackjeeves.ui.webview.WebViewFragment
import skizo.library.extensions.formatDate
import com.smackjeeves.utils.newFragment
import java.util.*

data class News(override var result: Result, var data: NewsData): BaseResponse()
data class NewsData(var news: List<NewsContent>)
data class NewsContent(var id: Int, var category: String, var caption: String, var releaseTime: Date, var url: String) {

    val date: String get() = releaseTime.formatDate

    fun onClick() {
        newFragment<WebViewFragment>(WebViewFragment.getBundle(url, "News"))
    }

}

data class NewsCategories(override var result: Result, var data: NewsCategorieData): BaseResponse()
data class NewsCategorieData(var categories: List<NewsCategoryContent>)
data class NewsCategoryContent(var key: String, var name: String)
