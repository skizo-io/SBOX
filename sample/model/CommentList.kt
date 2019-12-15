package com.smackjeeves.model

import com.smackjeeves.model.base.BaseResponse
import com.smackjeeves.model.base.Result
import com.smackjeeves.model.item.Comment

data class CommentList(override var result: Result, var data: CommentData): BaseResponse()

data class CommentData(var comments: List<Comment>)

