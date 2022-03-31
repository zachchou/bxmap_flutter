part of bxmap_flutter;



const VIEW_TYPE = 'com.bixiu.bxmap_flutter';
final MethodChannelBXMapFlutterMap _methodChannel = BXMapFLutterPlatform.instance as MethodChannelBXMapFlutterMap;

class BXMapController {
  final int mapId;
  final _MapState _mapState;

  BXMapController._(CameraPosition initCameraPosition, this._mapState, {required this.mapId}) {
    _connectStreams(mapId);
  }

  static Future<BXMapController> init(
      int id,
      CameraPosition initialCameration,
      _MapState mapState,
      ) async {
    await _methodChannel.init(id);
    return BXMapController._(
        initialCameration,
        mapState,
        mapId: id,
    );
  }

  void _connectStreams(int mapId) {
    if (_mapState.widget.onCameraMoveEnd != null) {
      _methodChannel
          .onCameraMoveEnd(mapId: mapId)
          .listen((CameraPositionMoveEndEvent e) => _mapState.widget.onCameraMoveEnd!(e.value));
    }

    if (_mapState.widget.onMapPoiTap != null) {
      _methodChannel
      .onMapPoiTouch(mapId: mapId)
          .listen((MapPoiTouchEvent e) => _mapState.widget.onMapPoiTap!(e.value));
    }
  }

  void dispose() {
    _methodChannel.dispose(id: mapId);
  }

  Future<void> cameraChange(CameraPosition cameraPosition) async {
    return _methodChannel.cameraChange(cameraPosition, mapId: mapId);
  }
}