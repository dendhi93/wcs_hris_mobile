package com.wcs.mobilehris.application

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import androidx.room.Room
import com.wcs.mobilehris.database.maindatabase.MainDatabase


class WcsHrisApps : Application(){

    companion object{
        lateinit var  database : MainDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this, MainDatabase::class.java, "wcs_hris.db").build()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}