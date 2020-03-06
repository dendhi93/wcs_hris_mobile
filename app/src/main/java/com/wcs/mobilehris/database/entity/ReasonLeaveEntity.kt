package com.wcs.mobilehris.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mReason_leave")
data class ReasonLeaveEntity(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "mLeaveCode") var mLeaveCode : String,
    @ColumnInfo(name = "mLeaveDescription") var mLeaveDescription : String,
    @ColumnInfo(name = "mLeaveUpdateDate") var mLeaveUpdateDate : String
)