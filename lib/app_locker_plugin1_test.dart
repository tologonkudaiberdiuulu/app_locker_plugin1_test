import 'app_locker_plugin1_test_platform_interface.dart';
import 'package:flutter/services.dart';

class AppLockerPlugin1Test {
  static const MethodChannel _channel = MethodChannel('app_locker_plugin');

  Future<String?> getPlatformVersion() {
    return AppLockerPlugin1TestPlatform.instance.getPlatformVersion();
  }

  // Get the list of locked apps
  static Future<List<String>> getLockedApps() async {
    final List<dynamic> lockedApps = await _channel.invokeMethod(
      'getLockedApps',
    );
    return lockedApps.cast<String>();
  }

  // Add an app to the locked apps list
  static Future<void> addLockedApp(String packageName) async {
    await _channel.invokeMethod('addLockedApp', {'packageName': packageName});
  }

  // Remove an app from the locked apps list
  static Future<void> removeLockedApp(String packageName) async {
    await _channel.invokeMethod('removeLockedApp', {
      'packageName': packageName,
    });
  }

  // Notify the native side that the app is unlocked after completing the quiz
  static Future<void> notifyAppUnlocked() async {
    await _channel.invokeMethod('notifyAppUnlocked');
  }
}
