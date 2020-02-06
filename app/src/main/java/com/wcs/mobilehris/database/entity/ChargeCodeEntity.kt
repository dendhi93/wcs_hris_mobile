package com.wcs.mobilehris.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mCharge_code")
data class ChargeCodeEntity(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "mChargeCodeNo") var mChargeCodeNo : String,
    @ColumnInfo(name = "mDescriptionChargeCode") var mDescriptionChargeCode :String,
    @ColumnInfo(name = "mCompanyName") var mCompanyName : String,
    @ColumnInfo(name = "mProjectManager") var mProjectManager : String,
    @ColumnInfo(name = "mUpdateDate") var mUpdateDate : String
    )