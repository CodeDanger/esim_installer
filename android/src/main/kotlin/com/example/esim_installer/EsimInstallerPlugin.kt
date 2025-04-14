package com.example.esim_installer

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.telephony.euicc.DownloadableSubscription
import android.telephony.euicc.EuiccManager
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class EsimInstallerPlugin : FlutterPlugin, MethodChannel.MethodCallHandler {
    private lateinit var channel: MethodChannel
    private lateinit var context: Context

    override fun onAttachedToEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        context = binding.applicationContext
        channel = MethodChannel(binding.binaryMessenger, \"com.esim.installer/channel\")
        channel.setMethodCallHandler(this)
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: MethodChannel.Result) {
        if (call.method == \"installEsim\") {
            val activationCode = call.argument<String>(\"activationCode\") ?: \"\"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val euiccManager = context.getSystemService(EuiccManager::class.java)
                if (euiccManager != null && EuiccManager.isEnabled()) {
                    val subscription = DownloadableSubscription.Builder(Uri.parse(activationCode)).build()
                    val intent = PendingIntent.getActivity(context, 0, Intent(), PendingIntent.FLAG_IMMUTABLE)
                    euiccManager.downloadSubscription(subscription, true, intent)
                    result.success(true)
                } else {
                    result.success(false)
                }
            } else {
                result.success(false)
            }
        } else {
            result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }
}
