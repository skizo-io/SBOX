package com.smackjeeves.model

import com.smackjeeves.model.base.BaseResponse
import com.smackjeeves.model.base.Result

data class AppInfo(override var result: Result, var data: AppInfoData): BaseResponse()

data class AppInfoData(
    var version: Version,
    var appDownloadURL: String)

data class Version(
    var max: Int,
    var min: Int,
    var display: String
)

