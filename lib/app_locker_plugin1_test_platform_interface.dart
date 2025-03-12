import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'app_locker_plugin1_test_method_channel.dart';

abstract class AppLockerPlugin1TestPlatform extends PlatformInterface {
  /// Constructs a AppLockerPlugin1TestPlatform.
  AppLockerPlugin1TestPlatform() : super(token: _token);

  static final Object _token = Object();

  static AppLockerPlugin1TestPlatform _instance = MethodChannelAppLockerPlugin1Test();

  /// The default instance of [AppLockerPlugin1TestPlatform] to use.
  ///
  /// Defaults to [MethodChannelAppLockerPlugin1Test].
  static AppLockerPlugin1TestPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [AppLockerPlugin1TestPlatform] when
  /// they register themselves.
  static set instance(AppLockerPlugin1TestPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
