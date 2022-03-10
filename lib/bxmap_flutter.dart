// 毕宿五腾讯地图flutter插件入口文件
library bxmap_flutter;

import 'dart:async';

import 'package:bxmap_flutter/base/bxmap_utils.dart';
import 'package:bxmap_flutter/core/bxmap_flutter_platform.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/gestures.dart';
import 'package:flutter/services.dart';
import 'base/bxmap_api_key.dart';
import 'base/ui.dart';
import 'core/method_channel_bxmap_flutter.dart';
import 'base/camera.dart';
import 'base/location.dart';
import 'core/map_event.dart';


part 'src/bxmap_controller.dart';
part 'src/bxmap_widget.dart';

class BxmapFlutter {
  static const MethodChannel _channel =
      const MethodChannel('bxmap_flutter');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
