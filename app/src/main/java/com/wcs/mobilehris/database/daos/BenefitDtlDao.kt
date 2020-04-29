package com.wcs.mobilehris.database.daos

import androidx.room.*
import com.wcs.mobilehris.database.entity.BenefitDtlEntity

@Dao
interface BenefitDtlDao {
    @Query("SELECT * FROM tBenefitDtl WHERE tBenefitDocNo LIKE :title")
    fun getBenefitByDoc(title : String) : List<BenefitDtlEntity>

    @Query("SELECT tBenefitDocNo FROM tBenefitDtl WHERE tBenefitDocNo LIKE :title")
    fun getBenefitDocData(title : String) : String

    @Query("DELETE FROM tBenefitDtl")
    fun deleteAlltBenefitDtl()

    @Query("DELETE FROM tBenefitDtl where id = :idBenefit")
    fun deleteBenefitById(idBenefit : Int)

    @Query("SELECT count(id) FROM tBenefitDtl")
    fun getCountBenefitDtl() : Int

    @Query("SELECT max(id) FROM tBenefitDtl")
    fun getMaxIdBenefitDtl() : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBenfitDtl(benefitDtlEntity: BenefitDtlEntity)

    @Update
    fun updateBenefitDtl(benefitDtlEntity: BenefitDtlEntity)

    @Query("UPDATE tBenefitDtl set tBenefitDtlDate = :benefDate, tBenefitType = :benefType, tBenefitName = :benefName, tPersonalBenefit = :benefPerson, tAmountClaim = :benefAmount, tPaidClaim = :benefPaid, tDiagnoseDisease = :benefDiagnose, tBenefitDescription = :benefDescription, tBenefitDocNo = :benefDocNo  where id = :idBenefit")
    fun updateBenefitById(idBenefit: Int,
                          benefDate : String,
                          benefType : String,
                          benefName : String,
                          benefPerson : String,
                          benefAmount : String,
                          benefPaid : String,
                          benefDiagnose : String,
                          benefDescription :String,
                          benefDocNo : String)
}