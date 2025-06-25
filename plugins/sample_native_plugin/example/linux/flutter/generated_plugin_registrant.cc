//
//  Generated file. Do not edit.
//

// clang-format off

#include "generated_plugin_registrant.h"

#include <sample_native_plugin/sample_native_plugin.h>

void fl_register_plugins(FlPluginRegistry* registry) {
  g_autoptr(FlPluginRegistrar) sample_native_plugin_registrar =
      fl_plugin_registry_get_registrar_for_plugin(registry, "SampleNativePlugin");
  sample_native_plugin_register_with_registrar(sample_native_plugin_registrar);
}
