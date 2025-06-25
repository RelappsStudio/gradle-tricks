#include "include/sample_native_plugin/sample_native_plugin_c_api.h"

#include <flutter/plugin_registrar_windows.h>

#include "sample_native_plugin.h"

void SampleNativePluginCApiRegisterWithRegistrar(
    FlutterDesktopPluginRegistrarRef registrar) {
  sample_native_plugin::SampleNativePlugin::RegisterWithRegistrar(
      flutter::PluginRegistrarManager::GetInstance()
          ->GetRegistrar<flutter::PluginRegistrarWindows>(registrar));
}
