package com.smackjeeves.model

import com.smackjeeves.model.base.BaseResponse
import com.smackjeeves.model.base.Result

data class TrendWords(override var result: Result, var data: TrendWordsData) : BaseResponse()

data class TrendWordsData(var trendWords: List<String>)