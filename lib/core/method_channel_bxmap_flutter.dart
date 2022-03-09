
import 'package:bxmap_flutter/core/bxmap_flutter_platform.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:flutter/src/foundation/basic_types.dart';
import 'package:flutter/src/gestures/recognizer.dart';
import 'package:flutter/src/widgets/framework.dart';

const VIEW_TYPE = 'com.bixiu.bxmap_flutter';

///使用[MethodChannel]与Native代码通信的[BXMapFlutterPlatform]的实现。
class MethodChannelBXMapFlutterMap implements BXMapFLutterPlatform {
  final Map<int, MethodChannel> _channels = {};

  MethodChannel channel(int mapId) {
    return _channels[mapId]!;
  }

  @override
  Future<void> init(int mapId) {
    MethodChannel? channel = _channels[mapId];
    if (channel == null) {
      channel = MethodChannel('bxmap_flutter_map_$mapId');
      channel.setMethodCallHandler((call) => _handleMethodCall(call, mapId));
      _channels[mapId] = channel;
    }
    return channel.invokeMethod<void>('map#waitForMap');
  }

  Future<dynamic> _handleMethodCall(MethodCall call, int mapId) async {
    switch (call.method) {
      case 'location#changed':
        // try {
        //   _mapEventStreamController.add(LocationChangedEvent(
        //       mapId, AMapLocation.fromMap(call.arguments['location'])!));
        // } catch (e) {
        //   print("location#changed error=======>" + e.toString());
        // }
        break;

      case 'camera#onMove':
        // try {
        //   _mapEventStreamController.add(CameraPositionMoveEvent(
        //       mapId, CameraPosition.fromMap(call.arguments['position'])!));
        // } catch (e) {
        //   print("camera#onMove error===>" + e.toString());
        // }
        break;
      case 'camera#onMoveEnd':
        // try {
        //   _mapEventStreamController.add(CameraPositionMoveEndEvent(
        //       mapId, CameraPosition.fromMap(call.arguments['position'])!));
        // } catch (e) {
        //   print("camera#onMoveEnd error===>" + e.toString());
        // }
        break;
      case 'map#onTap':
        // _mapEventStreamController
        //     .add(MapTapEvent(mapId, LatLng.fromJson(call.arguments['latLng'])!));
        break;
      case 'map#onLongPress':
        // _mapEventStreamController.add(MapLongPressEvent(
        //     mapId, LatLng.fromJson(call.arguments['latLng'])!));
        break;

      case 'marker#onTap':
        // _mapEventStreamController.add(MarkerTapEvent(
        //   mapId,
        //   call.arguments['markerId'],
        // ));
        break;
      case 'marker#onDragEnd':
        // _mapEventStreamController.add(MarkerDragEndEvent(
        //     mapId,
        //     LatLng.fromJson(call.arguments['position'])!,
        //     call.arguments['markerId']));
        break;
      case 'polyline#onTap':
        // _mapEventStreamController
        //     .add(PolylineTapEvent(mapId, call.arguments['polylineId']));
        break;
      case 'map#onPoiTouched':
        // try {
        //   _mapEventStreamController.add(
        //       MapPoiTouchEvent(mapId, AMapPoi.fromJson(call.arguments['poi'])!));
        // } catch (e) {
        //   print('map#onPoiTouched error===>' + e.toString());
        // }
        break;
    }
  }

  @override
  Widget buildView(
      Map<String, dynamic> creationParams,
      Set<Factory<OneSequenceGestureRecognizer>> gestureRecognizers,
      void Function(int id) onPlatformViewCreated) {
    if (defaultTargetPlatform == TargetPlatform.android) {
      return AndroidView(
        viewType: VIEW_TYPE,
        onPlatformViewCreated: onPlatformViewCreated,
        gestureRecognizers: gestureRecognizers,
        creationParams: creationParams,
        creationParamsCodec: const StandardMessageCodec(),
      );
    } else if (defaultTargetPlatform == TargetPlatform.iOS) {
      return UiKitView(
        viewType: VIEW_TYPE,
        onPlatformViewCreated: onPlatformViewCreated,
        gestureRecognizers: gestureRecognizers,
        creationParams: creationParams,
        creationParamsCodec: const StandardMessageCodec(),
      );
    }
    return Text('当前平台:$defaultTargetPlatform, 不支持使用腾讯地图插件');
  }

  @override
  void dispose({required int id}) {
    if (_channels.containsKey(id)) {
      _channels.remove(id);
    }
  }
}