package com.smackjeeves.model.item

import com.smackjeeves.event.EventType
import skizo.library.extensions.dispatcherEvent
import com.smackjeeves.model.base.BaseResponse
import com.smackjeeves.model.base.Result
import com.smackjeeves.preferences.UserPreference
import java.util.*


data class Balance(override var result: Result, var data: BalanceData) : BaseResponse() {
    override fun sync() {
        data.balance.parsing()
    }
}

data class BalanceData(var balance: BalanceItem)

data class BalanceItem(var coin: CoinItem, var ticket: TicketItem) {
    fun parsing() {
        UserPreference.balanceCoin = coin
        UserPreference.balanceTicket = ticket

        dispatcherEvent(EventType.DATA_RELOAD_BALANCE)
    }
}

data class CoinItem(var pay: Int, var free: Int) {
    var value: Int = pay + free
    get() = pay + free
}

data class TicketItem(
    var leftSecondsDailyRefill: Long,
    var reward: Int, var daily: Int
) {
    var refillDailyTicketDate: Date = Date()
    get() {
        return Date(Date().time + leftSecondsDailyRefill * 1000)
    }

}
