package com.wcs.mobilehris.utilinterface

interface ActionInterface{
    fun enableUI(typeUI : Int)
    fun disableUI(typeUI : Int)

    interface ShowHideUI{
        fun hideUI(typeUI : Int)
        fun showUI(typeUI : Int)
    }
}