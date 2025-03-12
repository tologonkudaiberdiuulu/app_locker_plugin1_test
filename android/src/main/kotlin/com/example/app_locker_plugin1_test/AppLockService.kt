package com.example.app_locker_plugin1_test

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.annotation.Nullable
import io.flutter.embedding.android.FlutterActivity
import java.util.Collections

class AppLockService : Service() {
    private var handler: Handler? = null
    private var serviceIntegration: AppLockServiceIntegration? = null
    private var currentLockedApp = ""
    private var appUnlocked = false

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

        val notification = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle("App Lock Active")
            .setContentText("Protecting your apps")
            .setSmallIcon(android.R.drawable.ic_lock_lock)
            .build()

        startForeground(1, notification)

        serviceIntegration = AppLockServiceIntegration.getInstance(this)
        handler = Handler(Looper.getMainLooper())
        handler!!.post(checkForegroundRunnable)
    }

    private val foregroundApp: String
        get() {
            try {
                val usm = getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager
                val end = System.currentTimeMillis()
                val start = end - 1000 * 60
                val stats = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, start, end)

                if (stats != null && !stats.isEmpty()) {
                    var recentStats: UsageStats? = null
                    for (usageStats in stats) {
                        if (recentStats == null || recentStats.lastTimeUsed < usageStats.lastTimeUsed) {
                            recentStats = usageStats
                        }
                    }
                    return recentStats?.packageName ?: ""
                }
                return ""
            } catch (e: Exception) {
                Log.e("AppLockService", "Error getting foreground app: ${e.message}")
                return ""
            }
        }

    override fun onTaskRemoved(rootIntent: Intent) {
        val restartServiceIntent = Intent(this, AppLockService::class.java)
        restartServiceIntent.setPackage(packageName)
        startService(restartServiceIntent)
        super.onTaskRemoved(rootIntent)
    }

    private val checkForegroundRunnable: Runnable = object : Runnable {
        override fun run() {
            val foregroundApp: String = this@AppLockService.foregroundApp
            val lockedApps: List<String> = serviceIntegration?.getLockedApps() ?: emptyList()

            if (lockedApps.contains(foregroundApp)) {
                if (foregroundApp != currentLockedApp) {
                    currentLockedApp = foregroundApp
                    appUnlocked = false
                }

                if (!appUnlocked) {
                    val intent: Intent = FlutterActivity.createDefaultIntent(this@AppLockService)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)

                    serviceIntegration?.setUnlockCallback(object : AppLockServiceIntegration.UnlockCallback {
                        override fun onUnlock() {
                            appUnlocked = true
                        }
                    })
                }
            } else {
                currentLockedApp = ""
                appUnlocked = false
            }

            handler!!.postDelayed(this, 500)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    @Nullable
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        handler!!.removeCallbacks(checkForegroundRunnable)
        super.onDestroy()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "App Lock", NotificationManager.IMPORTANCE_LOW)
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_ID = "AppLockChannel"
    }
}