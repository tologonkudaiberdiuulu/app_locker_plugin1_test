import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:app_locker_plugin1_test/app_locker_plugin1_test_method_channel.dart';

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  MethodChannelAppLockerPlugin1Test platform = MethodChannelAppLockerPlugin1Test();
  const MethodChannel channel = MethodChannel('app_locker_plugin1_test');

  setUp(() {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger.setMockMethodCallHandler(
      channel,
      (MethodCall methodCall) async {
        return '42';
      },
    );
  });

  tearDown(() {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger.setMockMethodCallHandler(channel, null);
  });

  test('getPlatformVersion', () async {
    expect(await platform.getPlatformVersion(), '42');
  });
}
