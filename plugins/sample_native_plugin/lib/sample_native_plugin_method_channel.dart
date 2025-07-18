import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'sample_native_plugin_platform_interface.dart';

/// An implementation of [SampleNativePluginPlatform] that uses method channels.
class MethodChannelSampleNativePlugin extends SampleNativePluginPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('sample_native_plugin');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
