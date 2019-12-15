package com.smackjeeves.network.body

interface UserProfileBody

data class UserProfileInfo(
    var nickname: String,
    var birthYear: Int,
    var birthMonth: Int,
    var birthDay: Int
) : UserProfileBody
data class UserProfileEmail(var email: String) : UserProfileBody
data class UserProfileNotification(var isEmailNotification: Boolean) : UserProfileBody