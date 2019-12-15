package com.smackjeeves.model.item

import com.smackjeeves.model.base.BaseResponse
import com.smackjeeves.model.base.Result


data class Profile(override var result: Result, var data: ProfileData) : BaseResponse()

data class ProfileData(var profile: ProfileItem)

data class ProfileItem(var userNo: Int,
                       var userName: String,
                       var nickname: String,
                       var isGuest: Boolean,
                       var imageUrl: String,
                       var email: String,
                       var birthYear: Int,
                       var isEmailNotification: Boolean,
                       var policyAgreeVersion: Int,
                       var unopenedGiftCount: Int)
