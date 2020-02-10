package com.wcs.mobilehris.feature.requesttravellist

data class TravelListModel (
    val travelId : String,
    val depart : String,
    val arrival : String,
    val dateFrom : String,
    val dateInto : String,
    val statusTravel : String,
    val isOneWay : Boolean
)