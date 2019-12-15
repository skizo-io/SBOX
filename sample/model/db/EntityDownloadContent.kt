package com.smackjeeves.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "content")
class EntityDownloadContent(@PrimaryKey(autoGenerate = true) var id: Long?,
                            @ColumnInfo(name = "title_id") var titleId: Int,
                            @ColumnInfo(name = "article_id") var articleId: Int,
                            @ColumnInfo(name = "file_path") var filePath: String) {
    constructor(): this(null, titleId = 1, articleId = 1, filePath = "")
}