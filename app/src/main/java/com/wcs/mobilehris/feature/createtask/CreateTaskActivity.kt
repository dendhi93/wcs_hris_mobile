package com.wcs.mobilehris.feature.createtask

import android.R.layout.simple_spinner_item
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import android.widget.RadioGroup.OnCheckedChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.wcs.mobilehris.R
import com.wcs.mobilehris.databinding.ActivityCreateTaskBinding
import com.wcs.mobilehris.feature.dtltask.CustomDetailTaskAdapter
import com.wcs.mobilehris.feature.dtltask.FriendModel
import com.wcs.mobilehris.util.ConstantObject
import com.wcs.mobilehris.util.MessageUtils


class CreateTaskActivity : AppCompatActivity(), CreateTaskInterface {
    private lateinit var activityCreateTaskBinding: ActivityCreateTaskBinding
    private lateinit var dtlTaskAdapter : CustomDetailTaskAdapter
    private var arrTeamTaskList = ArrayList<FriendModel>()
    private var selectedTask : String = ""

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
    }

    private fun loadTaskSpinner(){
        val arrTaskType = arrayOf("Type Task","Prospect","Pre Sales", projectTask, supportTask)
        val adapter = ArrayAdapter(this, simple_spinner_item, arrTaskType)
        adapter.setDropDownViewResource(simple_spinner_item)
        activityCreateTaskBinding.spCreateTaskTypeTask.adapter = adapter
        activityCreateTaskBinding.spCreateTaskTypeTask.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,view: View,position: Int,id: Long) {
                selectedTask = adapter.getItem(position).toString()
                    when(selectedTask.trim()){
                        projectTask -> {
                            activityCreateTaskBinding.viewModel?.isHiddenSolmanTv?.set(true)
                            activityCreateTaskBinding.viewModel?.isHiddenPMTv?.set(false)
                        }
                        supportTask -> {
                            activityCreateTaskBinding.viewModel?.isHiddenSolmanTv?.set(false)
                            activityCreateTaskBinding.viewModel?.isHiddenPMTv?.set(true)
                        }
                        else -> {
                            activityCreateTaskBinding.viewModel?.isHiddenSolmanTv?.set(true)
                            activityCreateTaskBinding.viewModel?.isHiddenPMTv?.set(true)
                        }
                    }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun initRadio(){
        activityCreateTaskBinding.rgCreateTaskIsOnsite.setOnCheckedChangeListener{ group, checkedId ->
                val radio: RadioButton = findViewById(checkedId)
                when("${radio.text}"){
                    getString(R.string.on_site) -> activityCreateTaskBinding.viewModel?.isOnsiteTask?.set(true)
                    else -> activityCreateTaskBinding.viewModel?.isOnsiteTask?.set(false)
                }
            }
    }

    override fun onLoadTeam(listTeam: List<FriendModel>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
            ALERT_CREATE_TASK_NO_CONNECTION -> { MessageUtils.alertDialogDismiss(alertMessage, alertTitle, this)}
        }
    }

    override fun onResizeLayout(resizeType : Int) {
        when(resizeType){
            matchParentSize -> {
                val lp = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 450)
                activityCreateTaskBinding.rlCreateTaskTop.layoutParams = lp
            }
            else ->{
                val lp = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
                activityCreateTaskBinding.rlCreateTaskTop.layoutParams = lp
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
        const val projectTask = "Project"
        const val supportTask = "Support"
    }
}
