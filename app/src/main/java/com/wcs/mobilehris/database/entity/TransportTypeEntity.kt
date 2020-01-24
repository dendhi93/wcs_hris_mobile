package com.wcs.mobilehris.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mTrans_type")
data class TransportTypeEntity (
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "mTransCode") var mTransCode : String,
    @ColumnInfo(name = "mTransTypeDescription") var mTransTypeDescription : String,
    @ColumnInfo(name = "mTransUpdateDate") var mTransUpdateDate : String
    )