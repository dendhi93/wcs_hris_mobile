package com.wcs.mobilehris.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wcs.mobilehris.database.entity.TravelRequestEntity

@Dao
interface TravelRequestDao {
    @Query("SELECT * FROM tTravel_request WHERE tChargeCode LIKE :chargeCode")
    fun getDtlTravelReq(chargeCode : String) : List<TravelRequestEntity>

    @Query("SELECT max(id) FROM tTravel_request ")
    fun getTravelMaxId() : Int

    @Query("SELECT count(id) FROM tTravel_request WHERE tChargeCode LIKE :chargeCode")
    fun getCountTravel(chargeCode : String) : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTravel(travelReqEntity: TravelRequestEntity)

    @Query("DELETE FROM tTravel_request WHERE tChargeCode LIKE :chargeCode")
    fun deleteTravelReq(chargeCode : String)
}