package com.example.app_locker_plugin1_test;

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            val serviceIntent = Intent(context, AppLockService::class.java)
            context.startForegroundService(serviceIntent)
        }
    }
}