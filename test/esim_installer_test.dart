import 'package:flutter_test/flutter_test.dart';
import 'package:esim_installer/esim_installer.dart';
import 'package:esim_installer/esim_installer_platform_interface.dart';
import 'package:esim_installer/esim_installer_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockEsimInstallerPlatform
    with MockPlatformInterfaceMixin
    implements EsimInstallerPlatform {
  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final EsimInstallerPlatform initialPlatform = EsimInstallerPlatform.instance;

  test('$MethodChannelEsimInstaller is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelEsimInstaller>());
  });

  test('getPlatformVersion', () async {
    // EsimInstaller esimInstallerPlugin = EsimInstaller();
    MockEsimInstallerPlatform fakePlatform = MockEsimInstallerPlatform();
    EsimInstallerPlatform.instance = fakePlatform;

    expect(await EsimInstaller.getPlatformVersion(), '42');
  });
}
