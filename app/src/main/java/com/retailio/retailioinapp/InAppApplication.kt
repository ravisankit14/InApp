package com.retailio.retailioinapp

import android.app.Application
import com.retailio.retailioinapp.roomdb.InAppRoomDatabase

class InAppApplication: Application() {

    private var db: InAppRoomDatabase? = null

    override fun onCreate() {
        super.onCreate()
        db = InAppRoomDatabase.getInstance(applicationContext)
    }

    fun dbInstance(): InAppRoomDatabase{
        return db!!
    }

}