package com.smackjeeves.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [EntityDownloadContent::class], version = 1)
abstract class DBDownloadContent: RoomDatabase() {

    abstract fun daoDownloadContent(): DAODownloadContent

    companion object {
        private var INSTANCE: DBDownloadContent? = null

        fun getInstance(context: Context): DBDownloadContent? {
            if(INSTANCE == null) {
                synchronized(DBDownloadContent::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        DBDownloadContent::class.java, "content.db")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }

    }

}