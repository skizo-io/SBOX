package com.smackjeeves.network.body

data class DeleteBookshelfBody(val id: List<Int>) {
    var titles: ArrayList<DeleteBookshelfItem> = arrayListOf()
        get() {
            id.forEach {
                titles.add(DeleteBookshelfItem(it))
            }
            return titles
        }
}

data class DeleteBookshelfItem(var id: Int)