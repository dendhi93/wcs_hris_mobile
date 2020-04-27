package com.wcs.mobilehris.feature.benefitclaim.list.listDtl

data class BenefitDtlModel(
    val benefitDtlId : String,
    val benefitDtlDate : String,
    val benefitType : String,
    val benefitName : String,
    val personalBenefit : String,
    val amountClaim : String,
    val paidClaim : String,
    val diagnoseDisease : String,
    val benefitDescription : String
)