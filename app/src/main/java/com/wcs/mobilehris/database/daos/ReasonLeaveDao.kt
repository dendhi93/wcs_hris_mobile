package com.wcs.mobilehris.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wcs.mobilehris.database.entity.ReasonLeaveEntity

@Dao
interface ReasonLeaveDao {
    @Query("SELECT * FROM mReason_leave")
    fun getAllReasonLeave() : List<ReasonLeaveEntity>

    @Query("SELECT max(id) FROM mReason_leave")
    fun getReasonLeaveMaxId() : Int

    @Query("SELECT count(id) FROM mReason_leave")
    fun getCountReasonLeave() : Int

    @Query("SELECT * FROM mReason_leave WHERE mLeaveDescription LIKE :reasonTitle")
    fun getDtlReason(reasonTitle : String) : List<ReasonLeaveEntity>

    @Query("DELETE FROM mReason_leave")
    fun deleteAllReasonLeave()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReasonLeave(reasonLeaveEntity: ReasonLeaveEntity)
}