package com.wcs.mobilehris.feature.leave.list

data class LeaveListModel(
    val leaveId : String,
    val leaveDescription : String,
    val leaveDateFrom : String,
    val leaveDateInto : String,
    val leaveStatus : String,
    val leaveRequestor : String
)