package com.wcs.mobilehris.feature.requesttravellist

data class TravelListModel (
    val travelId : String,
    val reasonDesc : String,
    val departDate : String,
    val returnDate : String,
    val travelDescription : String,
    val travelBusinessType : String,
    val statusTravel : String,
    val requestor : String
)