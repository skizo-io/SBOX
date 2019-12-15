package com.smackjeeves.network.body

import com.smackjeeves.preferences.UserPreference

data class RecoveryBody(var refreshToken: String = UserPreference.refreshToken)