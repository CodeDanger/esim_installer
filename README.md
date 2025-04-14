# esim_installer

A Flutter plugin to install eSIM profiles on Android and open provisioning link on iOS.

## Features

- 📱 Install eSIM on Android using `activationCode`
- 🍎 Open Apple provisioning URL on iOS using `carddata`

## Usage

```dart
final result = await EsimInstaller.installEsim(code: 'LPA:your-activation-code');
```

Platform Support
Android ✅
iOS ✅

MIT License
