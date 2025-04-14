import 'dart:io';
import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:url_launcher/url_launcher.dart';

class EsimInstaller {
  static const MethodChannel _channel = MethodChannel(
    'com.esim.installer/channel',
  );

  /// Install eSIM using the provided activationCode (Android) or carddata (iOS)
  static Future<bool> installEsim({required String code}) async {
    if (Platform.isAndroid) {
      try {
        final result = await _channel.invokeMethod('installEsim', {
          'activationCode': code,
        });
        return result == true;
      } catch (e) {
        debugPrint('Android eSIM installation failed: \$e');
        return false;
      }
    } else if (Platform.isIOS) {
      final uri = Uri.parse(
        'https://esimsetup.apple.com/esim_qrcode_provisioning?carddata=${Uri.encodeComponent(code)}',
      );
      if (await canLaunchUrl(uri)) {
        return launchUrl(uri, mode: LaunchMode.externalApplication);
      } else {
        debugPrint('iOS provisioning URL could not be opened');
        return false;
      }
    } else {
      debugPrint('Unsupported platform');
      return false;
    }
  }

  static Future<String?> getPlatformVersion() async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<bool> isEsimSupported() async {
    final bool? isSupported = await _channel.invokeMethod('isEsimSupported');
    return isSupported ?? false;
  }
}
