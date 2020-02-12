package com.wcs.mobilehris.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wcs.mobilehris.database.entity.ReasonTravelEntity

@Dao
interface ReasonTravelDao {

    @Query("SELECT * FROM mReason_Travel")
    fun getAllReasonTravel() : List<ReasonTravelEntity>

    @Query("SELECT max(id) FROM mReason_Travel")
    fun getReasonTravelMaxId() : Int

    @Query("SELECT count(id) FROM mReason_Travel")
    fun getCountReasonTravel() : Int

    @Query("SELECT * FROM mReason_Travel WHERE mReasonDescription LIKE :reasonTitle")
    fun getDtlReason(reasonTitle : String) : List<ReasonTravelEntity>

    @Query("DELETE FROM mTrans_type")
    fun deleteAllReasonTravel()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReasonTravel(reasonTravelEntity: ReasonTravelEntity)
}