package com.wcs.mobilehris.feature.createtask

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.wcs.mobilehris.R
import com.wcs.mobilehris.connection.ApiRepo
import com.wcs.mobilehris.database.entity.ChargeCodeEntity
import com.wcs.mobilehris.databinding.ActivityCreateTaskBinding
import com.wcs.mobilehris.feature.dtltask.CustomDetailTaskAdapter
import com.wcs.mobilehris.feature.dtltask.FriendModel
import com.wcs.mobilehris.feature.menu.MenuActivity
import com.wcs.mobilehris.feature.team.activity.TeamActivity
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils
import kotlin.collections.ArrayList


@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class CreateTaskActivity : AppCompatActivity(), CreateTaskInterface, SelectedFriendInterface {
    private lateinit var activityCreateTaskBinding: ActivityCreateTaskBinding
    private lateinit var createTaskAdapter : CustomDetailTaskAdapter
    private var arrTeamTaskList = ArrayList<FriendModel>()
    private var arrCompletedText = ArrayList<String>()
    private var keyDialogActive = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityCreateTaskBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_task)
        activityCreateTaskBinding.viewModel = CreateTaskViewModel(this, this, ApiRepo())
        activityCreateTaskBinding.rcCreateTask.layoutManager = LinearLayoutManager(this)
    }

    override fun onStart() {
        super.onStart()
        activityCreateTaskBinding.rcCreateTask.setHasFixedSize(true)
        createTaskAdapter = CustomDetailTaskAdapter(this, arrTeamTaskList, ConstantObject.vCreateEdit)
        createTaskAdapter.initSelectedTeamCallback(this)
        activityCreateTaskBinding.rcCreateTask.adapter = createTaskAdapter
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.mipmap.ic_arrow_back)
        }
        activityCreateTaskBinding.viewModel?.initUI()
        activityCreateTaskBinding.viewModel?.initDataChargeCode()
    }

    override fun onLoadTeam(listTeam: List<FriendModel>) {
        when(arrTeamTaskList.size){
            0 -> {
                arrTeamTaskList.addAll(listTeam)
                createTaskAdapter.notifyDataSetChanged()
                onResizeLayout(noMatchParentSize)
            }
            else -> {
                val selectedFriendModel = listTeam[0]
                //ngecek data yang sama di array
                val isMatch = arrTeamTaskList.contains(selectedFriendModel)
                if(isMatch){
                    onMessage("Data already on the list", ConstantObject.vToastInfo)
                }else{
                        arrTeamTaskList.addAll(listTeam)
                        createTaskAdapter.notifyDataSetChanged()
                        onResizeLayout(noMatchParentSize)
                }
            }
        }
    }

    override fun onMessage(message: String, messageType: Int) {
        when(messageType){
            ConstantObject.vToastError -> MessageUtils.toastMessage(this, message, ConstantObject.vToastError)
            ConstantObject.vToastInfo -> MessageUtils.toastMessage(this, message, ConstantObject.vToastInfo)
            ConstantObject.vToastSuccess -> MessageUtils.toastMessage(this, message, ConstantObject.vToastSuccess)
            else -> MessageUtils.snackBarMessage(message,this, ConstantObject.vSnackBarWithButton)
        }
    }

    override fun onAlertCreateTask(alertMessage: String,alertTitle: String,intTypeActionAlert: Int) {
        when(intTypeActionAlert){
            ALERT_CREATE_TASK_NO_CONNECTION ->  MessageUtils.alertDialogDismiss(alertMessage, alertTitle, this)
            ALERT_CREATE_TASK_CONFIRMATION -> {
                keyDialogActive = ALERT_CREATE_TASK_CONFIRMATION
                MessageUtils.alertDialogOkCancel(alertMessage, alertTitle, this, this)
            }
        }
    }

    override fun onResizeLayout(resizeType : Int) {
        when(resizeType){
            matchParentSize -> {
                val lp = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
                activityCreateTaskBinding.rlCreateTaskTop.layoutParams = lp
            }
            else ->{
                val scale: Float = this.resources.displayMetrics.density
                val pixelHeight = 450 * scale + 0.5f
                val lp = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, pixelHeight.toInt())
                activityCreateTaskBinding.rlCreateTaskTop.layoutParams = lp
            }
        }
    }

    override fun onLoadChargeCode(listChargeCode: List<ChargeCodeEntity>) {
        arrCompletedText.clear()
        for(i in listChargeCode.indices){
            arrCompletedText.add(listChargeCode[i].mChargeCodeNo+"  "+listChargeCode[i].mDescriptionChargeCode)
        }
        val chargeCodeAdapter = ArrayAdapter<String>(this,
            android.R.layout.simple_dropdown_item_1line,
            arrCompletedText
        )
        activityCreateTaskBinding.actCreateTaskChargeCode.setAdapter(chargeCodeAdapter)
        activityCreateTaskBinding.actCreateTaskChargeCode.threshold = 1
        activityCreateTaskBinding.actCreateTaskChargeCode.onItemClickListener = AdapterView.OnItemClickListener{
                parent,view,position,id->
            val selectedItem = parent.getItemAtPosition(position).toString()
            val splitChargeCode = selectedItem.split("  ")[0]
            activityCreateTaskBinding.viewModel?.findDataCreateTask(splitChargeCode)
        }
    }

    override fun getTeamData() {
        val stDateFrom = activityCreateTaskBinding.viewModel?.stDateTaskFrom?.get()
        val stDateInto = activityCreateTaskBinding.viewModel?.stDateTaskInto?.get()
        if(stDateFrom == "" || stDateInto == ""){
            onMessage("Please fill Date From or Date Into", ConstantObject.vToastInfo)
        }else{
            val intent = Intent(this, TeamActivity::class.java)
            intent.putExtra(ConstantObject.extra_intent, ConstantObject.extra_fromIntentCreateTask)
            intent.putExtra(ConstantObject.extra_dateFrom_intent, stDateFrom?.trim())
            intent.putExtra(ConstantObject.extra_dateInto_intent, stDateInto?.trim())
            startActivityForResult(intent, RESULT_SUCCESS_CODE)
        }
    }

    override fun onSuccessCreateTask() {
        Handler().postDelayed({
            onMessage(getString(R.string.alert_transaction_success), ConstantObject.vToastSuccess)
            activityCreateTaskBinding.viewModel?.isProgressCreateTask?.set(false)
            val intent = Intent(this, MenuActivity::class.java)
            intent.putExtra(MenuActivity.EXTRA_CALLER_ACTIVITY_FLAG, MenuActivity.EXTRA_FLAG_DASHBOARD)
            startActivity(intent)
            finish()
        }, 2000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
                RESULT_SUCCESS_CODE -> {
                    data?.let {
                        val intentTeamName : String = it.getStringExtra(RESULT_EXTRA_TEAM_NAME).toString()
                        val intentTeamUserId : String = it.getStringExtra(RESULT_EXTRA_TEAM_USER_ID).toString()
                        val intentTeamStatus : String = it.getStringExtra(RESULT_EXTRA_TEAM_STATUS).toString()
                        activityCreateTaskBinding.viewModel?.validateTeam(intentTeamUserId, intentTeamName,intentTeamStatus)
                    }
                }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                activityCreateTaskBinding.viewModel?.onBackCreateTaskMenu()
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        activityCreateTaskBinding.viewModel?.onBackCreateTaskMenu()
        finish()
        super.onBackPressed()
    }

    companion object{
        const val ALERT_CREATE_TASK_NO_CONNECTION = 1
        const val matchParentSize = 2
        const val noMatchParentSize = 4
        const val RESULT_SUCCESS_CODE = 3
        const val RESULT_EXTRA_TEAM_NAME = "team_name"
        const val RESULT_EXTRA_TEAM_USER_ID = "team_user_id"
        const val RESULT_EXTRA_TEAM_STATUS = "team_status"
        const val ALERT_CREATE_TASK_CONFIRMATION = 5
        const val chooseDateFrom = "date_from"
        const val chooseDateInto = "date_into"
        const val chooseTimeFrom = "time_from"
        const val chooseTimeInto = "time_into"
    }

    override fun onPositiveClick(o: Any) {
        when(keyDialogActive){
            ALERT_CREATE_TASK_CONFIRMATION -> activityCreateTaskBinding.viewModel?.submitTask()
        }
    }

    override fun onNegativeClick(o: Any) {}

    override fun selectedItemFriend(friendModel: FriendModel) {
        arrTeamTaskList.remove(friendModel)
        createTaskAdapter.notifyDataSetChanged()
    }

    override fun selectedDisplayFriend(friendModel: FriendModel) {}
}
