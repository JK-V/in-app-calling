package com.jkv.in_appcalling

import android.app.Application

class InAppCallingApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        CallNotificationManager.createNotificationChannels(this)
    }
}