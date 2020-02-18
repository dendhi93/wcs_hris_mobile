package com.wcs.mobilehris.feature.requesttravel

data class ReqTravelModel (
    val depart : String,
    val arrival : String,
    val dateCheckIn : String,
    val dateCheckOut : String,
    val transType : String,
    val hotelName : String
)