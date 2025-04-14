package com.esim.installer

import android.app.Activity
import android.content.Context
import android.telephony.euicc.EuiccManager
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

class EsimInstallerPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {
    private lateinit var channel: MethodChannel
    private var context: Context? = null
    private var activity: Activity? = null

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "com.esim.installer/channel")
        channel.setMethodCallHandler(this)
        context = flutterPluginBinding.applicationContext
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        when (call.method) {
            "installEsim" -> {
                val activationCode = call.argument<String>("activationCode")
                if (activationCode != null) {
                    installEsim(activationCode, result)
                } else {
                    result.error("INVALID_ARGUMENT", "Activation code is null", null)
                }
            }
            "isEsimSupported" -> {
                result.success(isEsimSupported())
            }
            "getPlatformVersion" -> {
                result.success("Android ${android.os.Build.VERSION.RELEASE}")
            }
            else -> {
                result.notImplemented()
            }
        }
    }

    private fun installEsim(activationCode: String, result: Result) {
        if (activity == null) {
            result.error("NO_ACTIVITY", "No activity available", null)
            return
        }

        val euiccManager = context?.getSystemService(Context.EUICC_SERVICE) as? EuiccManager
        if (euiccManager == null) {
            result.error("EUICC_NOT_AVAILABLE", "eSIM not supported on this device", null)
            return
        }

        if (!euiccManager.isEnabled) {
            result.error("EUICC_DISABLED", "eSIM is disabled on this device", null)
            return
        }

        try {
            euiccManager.downloadSubscription(
                EuiccManager.DownloadableSubscription.forActivationCode(activationCode),
                true, // switchAfterDownload
                activity?.mainExecutor,
                object : EuiccManager.DownloadSubscriptionCallback() {
                    override fun onDownloadComplete() {
                        result.success(true)
                    }

                    override fun onDownloadFailed(errorCode: Int) {
                        result.error("DOWNLOAD_FAILED", "eSIM download failed with error code: $errorCode", null)
                    }
                }
            )
        } catch (e: Exception) {
            result.error("INSTALLATION_FAILED", "Failed to install eSIM: ${e.message}", null)
        }
    }

    private fun isEsimSupported(): Boolean {
        val euiccManager = context?.getSystemService(Context.EUICC_SERVICE) as? EuiccManager
        return euiccManager != null && euiccManager.isEnabled
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
        context = null
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity
    }

    override fun onDetachedFromActivityForConfigChanges() {
        activity = null
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        activity = binding.activity
    }

    override fun onDetachedFromActivity() {
        activity = null
    }
}