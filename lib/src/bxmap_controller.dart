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

  }
}