package com.example.app_locker_plugin1_test;


import android.content.Context
import android.content.Intent
import com.example.app_locker_plugin1_test.AppLockService
import java.util.ArrayList

class AppLockServiceIntegration internal constructor(private val context: Context) {
    private val lockedApps: MutableList<String> = ArrayList()
    private var unlockCallback: UnlockCallback? = null

    interface UnlockCallback {
        fun onUnlock()
    }

    init {
        // Initialize with default locked apps (can be loaded from storage in a real app)
        lockedApps.add("com.instagram.android")
        lockedApps.add("com.whatsapp")
    }

    fun getLockedApps(): List<String> {
        return ArrayList(lockedApps)
    }

    fun addLockedApp(packageName: String) {
        if (!lockedApps.contains(packageName)) {
            lockedApps.add(packageName)
        }
    }

    fun removeLockedApp(packageName: String) {
        lockedApps.remove(packageName)
    }

    fun setUnlockCallback(callback: UnlockCallback?) {
        this.unlockCallback = callback
    }

    fun notifyAppUnlocked() {
        if (unlockCallback != null) {
            unlockCallback!!.onUnlock()
        }
    }

    fun startAppLockService() {
        val serviceIntent = Intent(context, AppLockService::class.java)
        context.startForegroundService(serviceIntent)
    }

    companion object {
        private var instance: AppLockServiceIntegration? = null
        fun getInstance(context: Context?): AppLockServiceIntegration? {
            if (Companion.instance == null) {
                Companion.instance = AppLockServiceIntegration(context!!)
            }
            return Companion.instance
        }
    }
}