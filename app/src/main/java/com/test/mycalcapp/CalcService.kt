package com.test.mycalcapp

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

/**
 * Created by Don Muthiani on 2/22/21.
 * Copyright (c) 2021 Accuret. All rights reserved.

 */
class CalcService: Service() {
    private val binder: LocalBinder = LocalBinder()


    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    // Methods
    fun add(a: Int, b: Int) = a + b

    fun subtract(a: Int, b: Int) = a - b

    fun multiply(a: Int, b: Int) = a * b

    fun divide(a: Int, b: Int) = a / b

    /**
     * Class used for the client Binder. Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        // Return this instance of BoundService so clients can call public methods
        fun getService(): CalcService = this@CalcService
    }

}