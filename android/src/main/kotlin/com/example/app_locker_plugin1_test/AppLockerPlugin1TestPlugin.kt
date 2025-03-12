package com.example.app_locker_plugin1_test;


import android.content.Context
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

class AppLockerPlugin : FlutterPlugin, MethodCallHandler {
    private var channel: MethodChannel? = null
    private var context: Context? = null
    private var serviceIntegration: AppLockServiceIntegration? = null

    override fun onAttachedToEngine(@NonNull binding: FlutterPluginBinding) {
        channel = MethodChannel(binding.getBinaryMessenger(), "app_locker_plugin")
        channel.setMethodCallHandler(this)
        context = binding.getApplicationContext()
        serviceIntegration = AppLockServiceIntegration(context!!)
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        when (call.method) {
            "getLockedApps" -> result.success(serviceIntegration!!.getLockedApps())
            "addLockedApp" -> {
                val packageName: String = call.argument("packageName")
                serviceIntegration!!.addLockedApp(packageName)
                result.success(null)
            }

            "removeLockedApp" -> {
                val removePackageName: String = call.argument("packageName")
                serviceIntegration!!.removeLockedApp(removePackageName)
                result.success(null)
            }

            "notifyAppUnlocked" -> {
                serviceIntegration!!.notifyAppUnlocked()
                result.success(null)
            }

            else -> result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPluginBinding?) {
        channel.setMethodCallHandler(null)
    }
}