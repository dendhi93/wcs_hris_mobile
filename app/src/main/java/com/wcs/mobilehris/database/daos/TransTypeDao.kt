package com.wcs.mobilehris.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wcs.mobilehris.database.entity.TransportTypeEntity

@Dao
interface TransTypeDao {
    @Query("SELECT * FROM mTrans_type")
    fun getAllTransType() : List<TransportTypeEntity>

    @Query("SELECT max(id) FROM mTrans_type")
    fun getTransTypeMaxId() : Int

    @Query("SELECT count(id) FROM mTrans_type")
    fun getCountTransType() : Int

    @Query("SELECT mTransCode FROM mTrans_type WHERE mTransTypeDescription LIKE :transtTitle")
    fun getTransTypeCode(transtTitle : String) : String

    @Query("DELETE FROM mTrans_type")
    fun deleteAllTransType()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTransType(transTypeEntity: TransportTypeEntity)
}