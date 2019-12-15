package com.smackjeeves.model.item

import com.smackjeeves.model.base.BaseResponse
import com.smackjeeves.model.base.Result


data class Author(var id: Int, var name: String, var imgUrl: String, var patreon: Patreon)

data class Patreon(var url: String)