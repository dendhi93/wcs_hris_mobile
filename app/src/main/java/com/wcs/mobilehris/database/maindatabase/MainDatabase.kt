package com.wcs.mobilehris.database.maindatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wcs.mobilehris.database.daos.*
import com.wcs.mobilehris.database.entity.*

@Database(entities = [UpdateMasterEntity::class,
    ChargeCodeEntity::class,
    TransportTypeEntity::class,
    TravelRequestEntity::class,
    ReasonTravelEntity::class], version = 1)
abstract class MainDatabase : RoomDatabase(){
    abstract fun updateMasterDao() : UpdateMasterDao
    abstract fun chargeCodeDao() : ChargeCodeDao
    abstract fun transTypeDao() : TransTypeDao
    abstract fun travelReqDao() : TravelRequestDao
    abstract fun reasonTravelDao() : ReasonTravelDao
}