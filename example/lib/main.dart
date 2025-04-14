import 'package:flutter/material.dart';
import 'package:esim_installer/esim_installer.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(title: const Text('eSIM Installer Example')),
        body: Center(
          child: ElevatedButton(
            onPressed: () async {
              final result = await EsimInstaller.installEsim(
                code:
                    'LPA:1\$123456789012345678901234567890', // مثال لكود تنشيط
              );
              debugPrint(result ? 'تم التثبيت' : 'فشل التثبيت');
            },
            child: const Text('تثبيت eSIM'),
          ),
        ),
      ),
    );
  }
}
