package com.example.app_locker_plugin1_test

import android.content.Context
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPluginBinding // Added import

class AppLockerPlugin1TestPlugin : FlutterPlugin, MethodCallHandler {
    private var channel: MethodChannel? = null
    private var context: Context? = null
    private var serviceIntegration: AppLockServiceIntegration? = null

    override fun onAttachedToEngine(@NonNull binding: FlutterPluginBinding) {
        channel = MethodChannel(binding.binaryMessenger, "app_locker_plugin")
        channel?.setMethodCallHandler(this) // Safe call
        context = binding.applicationContext
        serviceIntegration = AppLockServiceIntegration.getInstance(context)
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        when (call.method) {
            "getLockedApps" -> result.success(serviceIntegration?.getLockedApps() ?: emptyList())
            "addLockedApp" -> {
                val packageName: String? = call.argument("packageName")
                if (packageName != null) {
                    serviceIntegration?.addLockedApp(packageName)
                    result.success(null)
                } else {
                    result.error("INVALID_ARGUMENT", "packageName is null", null)
                }
            }
            "removeLockedApp" -> {
                val packageName: String? = call.argument("packageName")
                if (packageName != null) {
                    serviceIntegration?.removeLockedApp(packageName)
                    result.success(null)
                } else {
                    result.error("INVALID_ARGUMENT", "packageName is null", null)
                }
            }
            "notifyAppUnlocked" -> {
                serviceIntegration?.notifyAppUnlocked()
                result.success(null)
            }
            else -> result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPluginBinding?) {
        channel?.setMethodCallHandler(null) // Safe call
    }
}