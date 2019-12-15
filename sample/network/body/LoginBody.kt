package com.smackjeeves.network.body

import com.smackjeeves.preferences.UserPreference

data class LoginBody(var sessionKey: String, var encString: String)