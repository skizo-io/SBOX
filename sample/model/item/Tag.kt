package com.smackjeeves.model.item

import android.view.View
import com.smackjeeves.model.base.BaseResponse
import com.smackjeeves.model.base.Result
import com.smackjeeves.ui.search.SearchActivity
import com.smackjeeves.ui.webview.WebViewFragment
import com.smackjeeves.utils.newActivity
import com.smackjeeves.utils.newFragment


data class Tag(var value: String) {



    fun onClick(v: View) {
        v.context.newActivity<SearchActivity>( SearchActivity.INTENT_KEYWORD.to("#" + value) )
    }

}