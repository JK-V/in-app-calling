package com.jkv.in_appcalling

import android.app.Application
import com.jkv.in_appcalling.ui.notification.CallNotificationManager

class InAppCallingApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        CallNotificationManager.createNotificationChannels(this)
    }
}