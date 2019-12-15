package com.smackjeeves.model.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query


@Dao
interface DAODownloadContent {

    @Query("SELECT * FROM content")
    fun getAll(): List<EntityDownloadContent>

    @Insert(onConflict = REPLACE)
    fun insert(content: EntityDownloadContent)

    @Query("DELETE from content")
    fun deleteAll()

}