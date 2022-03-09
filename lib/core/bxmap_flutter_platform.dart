import 'package:bxmap_flutter/core/method_channel_bxmap_flutter.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/gestures.dart';
import 'package:flutter/services.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

abstract class BXMapFLutterPlatform extends PlatformInterface {
  static final Object _token = Object();
  BXMapFLutterPlatform() : super(token: _token);
  static BXMapFLutterPlatform _instance = MethodChannelBXMapFlutterMap();

  static BXMapFLutterPlatform get instance => _instance;

  static set instance(BXMapFLutterPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<void> init(int id) {
    throw UnimplementedError('init() has not been implemented.');
  }

  void dispose({required int id}) {
    throw UnimplementedError('dispose() has not been implemented.');
  }

  Widget buildView(
      Map<String, dynamic> creationParams,
      Set<Factory<OneSequenceGestureRecognizer>> gestureRecognizers,
      PlatformViewCreatedCallback onPlatformViewCreated) {
    throw UnimplementedError('buildView() has not been implemented.');
  }
}