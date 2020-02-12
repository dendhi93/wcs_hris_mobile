package com.wcs.mobilehris.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mReason_Travel")
data class ReasonTravelEntity(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "mReasonCode") var mReasonCode : String,
    @ColumnInfo(name = "mReasonDescription") var mReasonDescription : String,
    @ColumnInfo(name = "mReasonUpdateDate") var mReasonUpdateDate : String
)