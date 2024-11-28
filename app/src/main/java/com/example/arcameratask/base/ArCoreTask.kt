package com.example.arcameratask.base

import android.app.Application

class ArCoreTask : Application() {


    companion object {
        var instance : ArCoreTask ?= null
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
    }
}