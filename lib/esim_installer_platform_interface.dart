import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'esim_installer_method_channel.dart';

abstract class EsimInstallerPlatform extends PlatformInterface {
  /// Constructs a EsimInstallerPlatform.
  EsimInstallerPlatform() : super(token: _token);

  static final Object _token = Object();

  static EsimInstallerPlatform _instance = MethodChannelEsimInstaller();

  /// The default instance of [EsimInstallerPlatform] to use.
  ///
  /// Defaults to [MethodChannelEsimInstaller].
  static EsimInstallerPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [EsimInstallerPlatform] when
  /// they register themselves.
  static set instance(EsimInstallerPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
