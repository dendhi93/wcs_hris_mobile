package com.wcs.mobilehris.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wcs.mobilehris.database.entity.BenefitDtlEntity

@Dao
interface BenefitDtlDao {
    @Query("SELECT * FROM tBenefitDtl WHERE tBenefitDocNo LIKE :title")
    fun getBenefitByDoc(title : String) : List<BenefitDtlEntity>

    @Query("SELECT tBenefitDocNo FROM tBenefitDtl WHERE tBenefitDocNo LIKE :title")
    fun getBenefitDocData(title : String) : String

    @Query("DELETE FROM tBenefitDtl")
    fun deleteAlltBenefitDtl()

    @Query("SELECT count(id) FROM tBenefitDtl")
    fun getCountBenefitDtl() : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBenfitDtl(benefitDtlEntity: BenefitDtlEntity)
}