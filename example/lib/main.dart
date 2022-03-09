import 'dart:ui';

import 'package:bxmap_flutter/base/bxmap_api_key.dart';
import 'package:bxmap_flutter/base/camera.dart';
import 'package:bxmap_flutter/base/location.dart';
import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:bxmap_flutter/bxmap_flutter.dart';
import 'package:permission_handler/permission_handler.dart';

void main() {

  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    super.initState();
    // initPlatformState();

    requestPermission();

  }

  /// 动态申请定位权限
  void requestPermission() async {
    // 申请权限
    bool hasLocationPermission = await requestLocationPermission();
    if (hasLocationPermission) {
      print("定位权限申请通过");
    } else {
      print("定位权限申请不通过");
    }
  }

  /// 申请定位权限
  /// 授予定位权限返回true， 否则返回false
  Future<bool> requestLocationPermission() async {
    //获取当前的权限
    var status = await Permission.location.status;
    if (status == PermissionStatus.granted) {
      //已经授权
      return true;
    } else {
      //未授权则发起一次申请
      status = await Permission.location.request();
      if (status == PermissionStatus.granted) {
        return true;
      } else {
        return false;
      }
    }
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  // Future<void> initPlatformState() async {
  //   String platformVersion;
  //   // Platform messages may fail, so we use a try/catch PlatformException.
  //   // We also handle the message potentially returning null.
  //   try {
  //     platformVersion =
  //         await BxmapFlutter.platformVersion ?? 'Unknown platform version';
  //   } on PlatformException {
  //     platformVersion = 'Failed to get platform version.';
  //   }
  //
  //   // If the widget was removed from the tree while the asynchronous platform
  //   // message was in flight, we want to discard the reply rather than calling
  //   // setState to update our non-existent appearance.
  //   if (!mounted) return;
  //
  //   setState(() {
  //     _platformVersion = platformVersion;
  //   });
  // }

  @override
  Widget build(BuildContext context) {
    // final CameraPosition _bInitialPositon = CameraPosition(target: LatLng(26.038065, 119.370293));
    final height = window.physicalSize.height;
    final dpr = window.devicePixelRatio;
    final screenHeight = height / dpr;
    print("height: $height---dpr: $dpr----screenHeight: $screenHeight");
    final BXMapWidget map = BXMapWidget(
      apiKey: BXMapApiKey(androidKey: 'EEABZ-OXQLJ-S3ZFV-FFLSZ-UMT5Q-26FI2', iosKey: 'EEABZ-OXQLJ-S3ZFV-FFLSZ-UMT5Q-26FI2'),
      // initialCameraPosition: _bInitialPositon,
    );
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Container(
          height: screenHeight * 3 / 5,
          child: Stack(
            children: [
              map
            ],
          ),
        ),
      ),
    );
  }
}
