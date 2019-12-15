package com.smackjeeves.model

import com.smackjeeves.model.base.BaseResponse
import com.smackjeeves.model.base.Result
import com.smackjeeves.model.item.BalanceItem
import com.smackjeeves.model.item.ProfileItem

data class UserInfo(override var result: Result, var data: UserInfoData): BaseResponse() {
    override fun sync() {
        data.balance.parsing()
    }
}

data class UserInfoData(var balance: BalanceItem, var profile: ProfileItem)