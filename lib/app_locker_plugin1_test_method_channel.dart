import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'app_locker_plugin1_test_platform_interface.dart';

/// An implementation of [AppLockerPlugin1TestPlatform] that uses method channels.
class MethodChannelAppLockerPlugin1Test extends AppLockerPlugin1TestPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('app_locker_plugin1_test');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
