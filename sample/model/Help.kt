package com.smackjeeves.model

import com.smackjeeves.model.base.BaseResponse
import com.smackjeeves.model.base.Result

data class Help(override var result: Result, var data: HelpData) : BaseResponse()

data class HelpData(var help: List<HelpContent>)
data class HelpContent(var caption: String, var url: String)