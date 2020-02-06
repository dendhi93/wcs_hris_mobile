package com.wcs.mobilehris.feature.multipletrip

import com.wcs.mobilehris.database.entity.TravelRequestEntity

interface MultiTripInterface {
    fun onLoadTripList(multiTripList : List<TravelRequestEntity>)
    fun onMessage(message : String, messageType : Int)
    fun onAlertMultiTrip(alertMessage : String, alertTitle : String, intTypeActionAlert : Int)
    fun onSuccessTravel()
}