package com.smackjeeves.model

import com.google.gson.annotations.SerializedName
import com.smackjeeves.model.base.BaseResponse
import com.smackjeeves.model.base.Result

data class Default(override var result: Result, @SerializedName("data") var empty: DefaultData): BaseResponse()

data class DefaultData(var empty: Any?)
