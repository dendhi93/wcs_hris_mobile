package com.wcs.mobilehris.database.daos

import androidx.room.*
import com.wcs.mobilehris.database.entity.ChargeCodeEntity

@Dao
interface ChargeCodeDao{

    @Query("SELECT * FROM mCharge_code")
    fun getAllChargeCode() : List<ChargeCodeEntity>

    @Query("SELECT max(id) FROM mCharge_code")
    fun getMaxId() : Int

    @Query("SELECT count(id) FROM mCharge_code")
    fun getCountChargeCode() : Int

    @Query("SELECT mCompanyName FROM mCharge_code WHERE mChargeCodeNo LIKE :title")
    fun getCompName(title : String) : String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChargeCode(chargeCodeEntity: ChargeCodeEntity)

    @Delete
    fun deleteChargeCode(chargeCodeEntity: ChargeCodeEntity)

    @Query("DELETE FROM mCharge_code")
    fun deleteAllChargeCode()

}