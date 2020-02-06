package com.wcs.mobilehris.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tTravel_request")
data class TravelRequestEntity(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "tChargeCode") var tCharge_code : String,
    @ColumnInfo(name = "tDepartDate") var tdepartDate : String,
    @ColumnInfo(name = "tReturnDate") var tReturnDate : String,
    @ColumnInfo(name = "tDepartCity") var tDepartCity : String,
    @ColumnInfo(name = "tReturnCity") var tReturnCity : String,
    @ColumnInfo(name = "tFriends") var tFriends : String
    )