import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'esim_installer_platform_interface.dart';

/// An implementation of [EsimInstallerPlatform] that uses method channels.
class MethodChannelEsimInstaller extends EsimInstallerPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('esim_installer');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
