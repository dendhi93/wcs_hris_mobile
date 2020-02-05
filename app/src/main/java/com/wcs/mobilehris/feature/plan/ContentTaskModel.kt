package com.wcs.mobilehris.feature.plan

data class ContentTaskModel(
    val taskType : String,
    val userCreate : String,
    val createDate : String,
    val companyName : String,
    val beginTaskTime : String,
    val endTaskTime : String,
    val flagTask : String,
    val taskDate : String,
    val taskId : String,
    val actHourTaken : Int,
    val isCompleted : Boolean,
    val completedDescription : String
)