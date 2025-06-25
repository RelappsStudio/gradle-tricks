import 'package:flutter_test/flutter_test.dart';
import 'package:sample_native_plugin/sample_native_plugin.dart';
import 'package:sample_native_plugin/sample_native_plugin_platform_interface.dart';
import 'package:sample_native_plugin/sample_native_plugin_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockSampleNativePluginPlatform
    with MockPlatformInterfaceMixin
    implements SampleNativePluginPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final SampleNativePluginPlatform initialPlatform = SampleNativePluginPlatform.instance;

  test('$MethodChannelSampleNativePlugin is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelSampleNativePlugin>());
  });

  test('getPlatformVersion', () async {
    SampleNativePlugin sampleNativePlugin = SampleNativePlugin();
    MockSampleNativePluginPlatform fakePlatform = MockSampleNativePluginPlatform();
    SampleNativePluginPlatform.instance = fakePlatform;

    expect(await sampleNativePlugin.getPlatformVersion(), '42');
  });
}
