package com.wcs.mobilehris.feature.createtask

import android.R.layout.simple_spinner_item
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.wcs.mobilehris.R
import com.wcs.mobilehris.database.entity.ChargeCodeEntity
import com.wcs.mobilehris.databinding.ActivityCreateTaskBinding
import com.wcs.mobilehris.feature.dtltask.CustomDetailTaskAdapter
import com.wcs.mobilehris.feature.dtltask.FriendModel
import com.wcs.mobilehris.feature.team.TeamActivity
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils
import com.wcs.mobilehris.utilinterface.DialogInterface


class CreateTaskActivity : AppCompatActivity(), CreateTaskInterface, DialogInterface {
    private lateinit var activityCreateTaskBinding: ActivityCreateTaskBinding
    private lateinit var dtlTaskAdapter : CustomDetailTaskAdapter
    private var arrTeamTaskList = ArrayList<FriendModel>()
    private var arrCompletedText = ArrayList<String>()
    private var keyDialogActive = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityCreateTaskBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_task)
        activityCreateTaskBinding.viewModel = CreateTaskViewModel(this, this)
        activityCreateTaskBinding.rcCreateTask.layoutManager = LinearLayoutManager(this)
    }

    override fun onStart() {
        super.onStart()
        activityCreateTaskBinding.rcCreateTask.setHasFixedSize(true)
        dtlTaskAdapter = CustomDetailTaskAdapter(this, arrTeamTaskList)
        activityCreateTaskBinding.rcCreateTask.adapter = dtlTaskAdapter
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.mipmap.ic_arrow_back)
        }
        activityCreateTaskBinding.viewModel?.initUI()
        loadTaskSpinner()
        initRadio()
        activityCreateTaskBinding.viewModel?.initDataChargeCode()
    }

    private fun loadTaskSpinner(){
        val arrTaskType = arrayOf("Type Task",ConstantObject.prospectTask,ConstantObject.preSalesTask, ConstantObject.projectTask, ConstantObject.supportTask)
        val adapter = ArrayAdapter(this, simple_spinner_item, arrTaskType)
        adapter.setDropDownViewResource(simple_spinner_item)
        activityCreateTaskBinding.spCreateTaskTypeTask.adapter = adapter
        activityCreateTaskBinding.spCreateTaskTypeTask.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,view: View,position: Int,id: Long) {
                activityCreateTaskBinding.viewModel?.getTypeTask(adapter.getItem(position).toString())
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun initRadio(){
        activityCreateTaskBinding.rgCreateTaskIsOnsite.setOnCheckedChangeListener{ group, checkedId ->
                val radio: RadioButton? = findViewById(checkedId)
                when("${radio?.text}"){
                    getString(R.string.on_site) -> activityCreateTaskBinding.viewModel?.isOnsiteTask?.set(true)
                    else -> activityCreateTaskBinding.viewModel?.isOnsiteTask?.set(false)
                }
            }
    }

    override fun onLoadTeam(listTeam: List<FriendModel>) {
        arrTeamTaskList.addAll(listTeam)
        dtlTaskAdapter.notifyDataSetChanged()
        
        onResizeLayout(noMatchParentSize)
    }

    override fun onMessage(message: String, messageType: Int) {
        when(messageType){
            ConstantObject.vToastError -> MessageUtils.toastMessage(this, message, ConstantObject.vToastError)
            ConstantObject.vToastInfo -> MessageUtils.toastMessage(this, message, ConstantObject.vToastInfo)
            ConstantObject.vSnackBarWithButton -> MessageUtils.snackBarMessage(message,this, ConstantObject.vSnackBarWithButton)
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
            activityCreateTaskBinding.viewModel?.findCompany(splitChargeCode)
        }
    }

    override fun getTeamData() { startActivityForResult(Intent(this, TeamActivity::class.java), RESULT_SUCCESS_CODE) }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
                RESULT_SUCCESS_CODE -> {
                    val intentTeamName : String = data?.getStringExtra(RESULT_EXTRA_TEAM_NAME).toString()
                    val intentTeamUserId : String = data?.getStringExtra(RESULT_EXTRA_TEAM_USER_ID).toString()
                    activityCreateTaskBinding.viewModel?.validateTeam(intentTeamUserId, intentTeamName)
                }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object{
        const val ALERT_CREATE_TASK_NO_CONNECTION = 1
        const val matchParentSize = 2
        const val noMatchParentSize = 4
        const val RESULT_SUCCESS_CODE = 3
        const val RESULT_EXTRA_TEAM_NAME = "team_name"
        const val RESULT_EXTRA_TEAM_USER_ID = "team_user_id"
        const val ALERT_CREATE_TASK_CONFIRMATION = 5
    }

    override fun onPositiveClick(o: Any) {
        when(keyDialogActive){
            ALERT_CREATE_TASK_CONFIRMATION -> activityCreateTaskBinding.viewModel?.submitTask()
        }
    }

    override fun onNegativeClick(o: Any) {}
}
