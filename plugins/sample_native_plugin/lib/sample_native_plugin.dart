
import 'sample_native_plugin_platform_interface.dart';

class SampleNativePlugin {
  Future<String?> getPlatformVersion() {
    return SampleNativePluginPlatform.instance.getPlatformVersion();
  }
}
