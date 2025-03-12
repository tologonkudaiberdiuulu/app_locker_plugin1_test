import 'package:flutter_test/flutter_test.dart';
import 'package:app_locker_plugin1_test/app_locker_plugin1_test.dart';
import 'package:app_locker_plugin1_test/app_locker_plugin1_test_platform_interface.dart';
import 'package:app_locker_plugin1_test/app_locker_plugin1_test_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockAppLockerPlugin1TestPlatform
    with MockPlatformInterfaceMixin
    implements AppLockerPlugin1TestPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final AppLockerPlugin1TestPlatform initialPlatform = AppLockerPlugin1TestPlatform.instance;

  test('$MethodChannelAppLockerPlugin1Test is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelAppLockerPlugin1Test>());
  });

  test('getPlatformVersion', () async {
    AppLockerPlugin1Test appLockerPlugin1TestPlugin = AppLockerPlugin1Test();
    MockAppLockerPlugin1TestPlatform fakePlatform = MockAppLockerPlugin1TestPlatform();
    AppLockerPlugin1TestPlatform.instance = fakePlatform;

    expect(await appLockerPlugin1TestPlugin.getPlatformVersion(), '42');
  });
}
