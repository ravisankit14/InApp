package com.retailio.retailioinapp.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.retailio.retailioinapp.SingletonHolder
import com.retailio.retailioinapp.roomdb.dao.NotificationInAppEntityDao
import com.retailio.retailioinapp.roomdb.entity.NotificationInAppEntity

@Database(entities = [NotificationInAppEntity::class],version = 1)
abstract class InAppRoomDatabase: RoomDatabase() {
    abstract fun notificationInAppDao(): NotificationInAppEntityDao

    fun delete(){
        notificationInAppDao().deleteAll()
    }

    companion object : SingletonHolder<InAppRoomDatabase, Context>({
        Room.databaseBuilder(it.applicationContext,
            InAppRoomDatabase::class.java, "in_app_database")
            .build()
    })
}
