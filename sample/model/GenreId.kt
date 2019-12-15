package com.smackjeeves.model

import com.smackjeeves.utils.Config
import com.smackjeeves.model.base.BaseResponse
import com.smackjeeves.model.base.Result
import com.smackjeeves.model.item.Genre
import com.smackjeeves.model.ui.ComicMainTab

data class GenreId(override var result: Result, var data: GenreIdData) : BaseResponse() {
    override fun sync() {
        data.genres.forEach {
            Config.comicMainTab.genres.add(ComicMainTab(it.id, it.name))
        }
    }
}

data class GenreIdData(var genres: List<Genre>)