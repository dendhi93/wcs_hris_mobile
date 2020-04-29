package com.wcs.mobilehris.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tBenefitDtl")
data class BenefitDtlEntity(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "tBenefitDtlDate") var tBenefitDtlDate : String,
    @ColumnInfo(name = "tBenefitType") var tBenefitType : String,
    @ColumnInfo(name = "tBenefitName") var tBenefitName : String,
    @ColumnInfo(name = "tPersonalBenefit") var tPersonalBenefit : String,
    @ColumnInfo(name = "tAmountClaim") var tAmountClaim : String,
    @ColumnInfo(name = "tPaidClaim") var tPaidClaim : String,
    @ColumnInfo(name = "tDiagnoseDisease") var tDiagnoseDisease : String,
    @ColumnInfo(name = "tBenefitDescription") var tBenefitDescription : String,
    @ColumnInfo(name = "tBenefitDocNo") var tBenefitDocNo : String
)