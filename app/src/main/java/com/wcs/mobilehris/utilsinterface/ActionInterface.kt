package com.wcs.mobilehris.utilsinterface

interface ActionInterface{
    fun enableUI(typeUI : Int)
    fun disableUI(typeUI : Int)

    interface showHideUI{
        fun hideUI(typeUI : Int)
        fun showUI(typeUI : Int)
    }
}