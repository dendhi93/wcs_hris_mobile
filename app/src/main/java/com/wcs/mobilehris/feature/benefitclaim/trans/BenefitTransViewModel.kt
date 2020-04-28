package com.wcs.mobilehris.feature.benefitclaim.trans

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import java.util.*

class BenefitTransViewModel(private val context: Context,
                            private val benefitTransactionInterface: BenefitTransactionInterface):ViewModel(){
    val isVisibleBenefitTransProgress = ObservableField(false)
    val isVisibleBenefitButton = ObservableField(false)
    val isEnableTv = ObservableField(false)
    val stBenefitDate = ObservableField("")
    val stBenefitTransName = ObservableField("")
    val stBenefitTransPerson = ObservableField("")
    val stBenefitTransDiagnose = ObservableField("")
    val stBenefitTransAmount = ObservableField("")
    val stBenefitPaidAmount = ObservableField("")
    val stBenefitTransDescription = ObservableField("")
    private val calendar : Calendar = Calendar.getInstance()
    private var mYear : Int = 0
    private var mMonth : Int = 0
    private var mDay : Int = 0

    fun initDataBenefit(benefTransType : String){
        when(benefTransType){
            BenefitTransActivity.extraValueTransDtlType -> {
                isVisibleBenefitButton.set(false)
                isEnableTv.set(false)
            }
            else -> {
                isVisibleBenefitButton.set(true)
                isEnableTv.set(true)
            }
        }
    }

    fun onInitDate(){
        mYear = calendar.get(Calendar.YEAR)
        mMonth = calendar.get(Calendar.MONTH)
        mDay = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(context,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                val selectedMonth: String = if (month < 10) {
                    "0" + (month + 1)
                } else {
                    month.toString()
                }
                val selectedDay: String = if (dayOfMonth < 10) {
                    "0$dayOfMonth"
                } else {
                    dayOfMonth.toString()
                }
                stBenefitDate.set("$year-$selectedMonth-$selectedDay")
            }, mYear, mMonth, mDay
        )
        datePickerDialog.show()
    }

    fun onClickAddBenefit(){}
}