package com.wcs.mobilehris.feature.benefitclaim.trans

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wcs.mobilehris.util.ConstantObject
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
        Log.d("###","" +benefTransType)
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

    fun onInitCurrency(){
        val listCur = mutableListOf<CurrencyModel>()
        listCur.add(CurrencyModel("IDR","IDR"))
        listCur.add(CurrencyModel("USD","USD"))
        listCur.add(CurrencyModel("SGD","SGD"))
        listCur.add(CurrencyModel("EUR","EUR"))
        listCur.add(CurrencyModel("AUR","AUD"))

        when(listCur.size){
            0 -> benefitTransactionInterface.onMessage("Currency No Found, please Contact support", ConstantObject.vToastError)
            else -> benefitTransactionInterface.onLoadCurrencySpinner(listCur)
        }
    }

    fun initBenefitName(){
        val listBenefName = mutableListOf<BenefitNameModel>()
        listBenefName.add(BenefitNameModel("RI","RAWAT INAP", "KO"))
        listBenefName.add(BenefitNameModel("RJ","RAWAT JALAN", "KO"))
        listBenefName.add(BenefitNameModel("MAT","MATERNITY", "PERSALINAN"))
        listBenefName.add(BenefitNameModel("MINSUR","MINOR SURGERY", "BEDAH KECIL"))
        listBenefName.add(BenefitNameModel("INSUR","INTERMEDIATE SURGERY", "BEDAH SEDANG"))
        listBenefName.add(BenefitNameModel("MASUR","MAJOR SURGERY", "BEDAH BESAR"))
        listBenefName.add(BenefitNameModel("FRM","FRAME KACAMATA", ""))
        listBenefName.add(BenefitNameModel("LEN","LENSA", ""))

        when(listBenefName.size){
            0 -> benefitTransactionInterface.onMessage("Transaction Name No Found, please Contact support", ConstantObject.vToastError)
            else -> benefitTransactionInterface.onLoadSpinnerTransName(listBenefName)
        }
    }

    fun initPerson(){
        val listPerson = mutableListOf<BenefitPersonModel>()
        listPerson.add(BenefitPersonModel("1","BUDI", "PERSONEL"))
        listPerson.add(BenefitPersonModel("2","ISTRI", "SPOUSE"))
        listPerson.add(BenefitPersonModel("3","CHILD 1", "CHILD"))
        listPerson.add(BenefitPersonModel("4","CHILD 2", "CHILD"))

        when(listPerson.size){
            0 -> benefitTransactionInterface.onMessage("Person No Found, please Contact support", ConstantObject.vToastError)
            else -> benefitTransactionInterface.onLoadSpinnerPerson(listPerson)
        }
    }

    fun initDiagnose(){
        val listDiagnose = mutableListOf<DiagnoseModel>()
        listDiagnose.add(DiagnoseModel("t01", "Perawatan Luka"))
        listDiagnose.add(DiagnoseModel("t02", "Pasca melahirkan caesar"))
        listDiagnose.add(DiagnoseModel("t69", "Gula Darah"))
        listDiagnose.add(DiagnoseModel("t65", "Pusing Panas Muntah"))
        listDiagnose.add(DiagnoseModel("t63", "Mulut"))
        listDiagnose.add(DiagnoseModel("t60", "Jiwa Klinik"))
        listDiagnose.add(DiagnoseModel("t034", "ISPA"))

        when(listDiagnose.size){
            0 -> benefitTransactionInterface.onMessage("diagnose No Found, please Contact support", ConstantObject.vToastError)
            else -> benefitTransactionInterface.onLoadSpinnerDiagnose(listDiagnose)
        }
    }

    fun onClickAddBenefit(){}
}