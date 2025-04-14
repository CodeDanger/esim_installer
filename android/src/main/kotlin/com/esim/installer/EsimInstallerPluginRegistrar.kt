package com.esim.installer

import androidx.annotation.Keep
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.PluginRegistry

@Keep
object EsimInstallerPluginRegistrar {
    @JvmStatic
    fun registerWith(registrar: PluginRegistry.Registrar) {
        val channel = MethodChannel(registrar.messenger(), "com.esim.installer/channel")
        channel.setMethodCallHandler(EsimInstallerPlugin())
    }
}