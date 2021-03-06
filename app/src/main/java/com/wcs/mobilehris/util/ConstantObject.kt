package com.wcs.mobilehris.util

object ConstantObject {
    //toast
    const val vToastSuccess = 1
    const val vToastError = 2
    const val vToastInfo = 3

    //snack bar
    const val vSnackBarWithButton = 11
    const val vSnackBarNoButton = 22

    //alertDialog
    const val vAlertDialogNoConnection = "No Connection"
    const val vAlertDialogConfirmation = "Confirmation"

    //UI type
    const val vButtonUI = 10
    const val vProgresBarUI = 20
    const val vEditTextUI = 30
    const val vGlobalUI = 99
    const val vRecylerViewUI = 5

    //type flagTask Type
    const val vPlanTask = "Plan"
    const val vConfirmTask = "Confirm"
    const val vCompletedTask = "Completed"
    const val vEditTask = "Edit"
    const val vApproved = "Approved"
    const val vRejected = "Rejected"
    const val vNew = "NEW"
    const val vWaitingTask = "WAITING"
    const val vLeaveApprove = "APPROVED"
    const val vNoApproval = "No Approval"
    const val vLeaveReject = "REJECT"

    //type progressBar
    const val vLoadWithProgressBar = 11
    const val vLoadWithoutProgressBar = 22

    //type task
    const val vSupportTask = "Support"
    const val vProjectTask = "Project"
    const val vProspectTask = "Prospect"
    const val vPreSalesTask = "PreSales"
    const val vCreateEdit = "createEdit"
    const val vNotCreateEdit = "nonCreateEdit"

    //menu request
    const val travelMenu = "TRAVEL"
    const val leaveMenu = "LEAVE"
    const val benefitMenu = "BENEFIT"
    const val travelClaimMenu = "TRAVEL CLAIM"
    const val activityMenu = "ACTIVITY"

    //type intent
    const val extra_intent = "extra_intent"
    const val extra_dateFrom_intent = "extra_dateFrom_intent"
    const val extra_dateInto_intent = "extra_dateInto_intent"
    const val extra_fromIntentCreateTask = "CreateTask"
    const val extra_fromIntentTeam = "team_fragment"
    const val extra_fromIntentProfile = "profile_activity"
    const val extra_fromIntentDtlTravel = "Detail Travel"
    const val extra_fromIntentConfirmTravel = "Confirm Travel"
    const val extra_fromIntentCreateTravel = "CreateTravel"
    const val extra_fromIntentRequest = "Request"
    const val extra_fromIntentApproval = "Approval"
    const val extra_fromIntentSearchTeam = "Team"
    const val extra_fromIntentSearchConflict = "Conflict"

    //response API
    const val vResponseData = "Data"
    const val vResponseResult = "result"
    const val vResponseMessage = "Message"
    const val vResponseStatus = "Status"
    const val vValueResponseSuccess = "Success"


    //param tabel
    const val keyChargeCode = "mChargecode"
    const val keyTransType = "mTrans_type"
    const val keyReasonTravel = "mReason"
    const val keyLeaveType = "mLeaveType"

    //date format from
    const val dateTimeFormat_1 = 1
    const val dateTimeFormat_2 = 2
    const val dateTimeFormat_3 = 3
    const val dateTimeFormat_4 = 4
}