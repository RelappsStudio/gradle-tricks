import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'sample_native_plugin_method_channel.dart';

abstract class SampleNativePluginPlatform extends PlatformInterface {
  /// Constructs a SampleNativePluginPlatform.
  SampleNativePluginPlatform() : super(token: _token);

  static final Object _token = Object();

  static SampleNativePluginPlatform _instance = MethodChannelSampleNativePlugin();

  /// The default instance of [SampleNativePluginPlatform] to use.
  ///
  /// Defaults to [MethodChannelSampleNativePlugin].
  static SampleNativePluginPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [SampleNativePluginPlatform] when
  /// they register themselves.
  static set instance(SampleNativePluginPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
