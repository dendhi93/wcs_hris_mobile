package com.wcs.mobilehris.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mUpdate_master_data")
data class UpdateMasterEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "mTableDescription") var mTableDescription: String,
    @ColumnInfo(name = "mUpdateDate") var mUpdateDate: String
)