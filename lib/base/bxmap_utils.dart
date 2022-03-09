import 'package:flutter/cupertino.dart';

class BXMapUtil {
  static BXMapUtil _instance = BXMapUtil._();
  static double _devicePixelRatio = 0;
  static void init(BuildContext context) {
    _devicePixelRatio = MediaQuery.of(context).devicePixelRatio;
  }

  BXMapUtil._();

  factory BXMapUtil() {
    return _instance;
  }

  ///获取当前设备的屏幕像素比
  static double get devicePixelRatio => _devicePixelRatio;
}