package com.wcs.mobilehris.database.maindatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wcs.mobilehris.database.daos.ChargeCodeDao
import com.wcs.mobilehris.database.daos.UpdateMasterDao
import com.wcs.mobilehris.database.entity.ChargeCodeEntity
import com.wcs.mobilehris.database.entity.UpdateMasterEntity

@Database(entities = [UpdateMasterEntity::class, ChargeCodeEntity::class], version = 1)
abstract class MainDatabase : RoomDatabase(){
    abstract fun updateMasterDao() : UpdateMasterDao
    abstract fun chargeCodeDao() : ChargeCodeDao
}