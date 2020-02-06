package com.wcs.mobilehris.feature.multipletrip

import android.content.Context
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.R
import com.wcs.mobilehris.application.WcsHrisApps
import com.wcs.mobilehris.connection.ConnectionObject
import com.wcs.mobilehris.database.daos.TravelRequestDao
import com.wcs.mobilehris.util.ConstantObject
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MultipleTripViewModel (private val context : Context, private val multipleTripInterface : MultiTripInterface) : ViewModel(){
    val isProgressMultipleTrip = ObservableField<Boolean>(false)
    val isLoadData = ObservableField<Boolean>(false)
    private lateinit var travelRequestDao : TravelRequestDao
    private lateinit var vChargeCode : String

    fun initTripData(chargeCode : String){
        travelRequestDao = WcsHrisApps.database.travelReqDao()
        vChargeCode = chargeCode
        isProgressMultipleTrip.set(true)
        isLoadData.set(false)
        doAsync {
            val list = travelRequestDao.getDtlTravelReq(chargeCode)
            uiThread {
                when{
                    list.isNotEmpty() -> multipleTripInterface.onLoadTripList(list)
                }
            }
        }
    }

    fun saveMultipleTrip(){
        when{
            !ConnectionObject.isNetworkAvailable(context) -> multipleTripInterface.onAlertMultiTrip(context.getString(
                R.string.alert_no_connection),
                ConstantObject.vAlertDialogNoConnection, MultipleTripActivity.ALERT_MULTITRIP_NO_CONNECTION)
            else -> {
                isProgressMultipleTrip.set(true)
                doAsync {
                    travelRequestDao.deleteTravelReq(vChargeCode.trim())
                    uiThread {
                        multipleTripInterface.onSuccessTravel()
                    }
                }
            }
        }
    }
}