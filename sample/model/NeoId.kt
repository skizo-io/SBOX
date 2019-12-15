package com.smackjeeves.model

import com.smackjeeves.model.base.BaseResponse
import com.smackjeeves.model.base.Result
import com.smackjeeves.preferences.UserPreference
import skizo.library.extensions.trace

data class NeoId(override var result: Result, var data: NeoIdData): BaseResponse() {
    override fun sync() {
        trace("##BaseApi## onResponse : NeoId")
        UserPreference.userNo = data.userNo
        UserPreference.neoNo = data.neoNo
        UserPreference.accessToken = data.accessToken
        UserPreference.refreshToken = data.refreshToken?:""
    }
}
data class NeoIdData(var userNo: String,
                     var neoNo: String,
                     var accessToken: String,
                     var refreshToken: String?)