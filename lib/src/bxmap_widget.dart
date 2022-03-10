part of bxmap_flutter;


typedef void MapCreatedCallback(BXMapController controller);
typedef void ArgumentCallback<T>(T argument);

///用于展示腾讯地图的Widget
class BXMapWidget extends StatefulWidget {
  ///腾讯开发平台的key
  final BXMapApiKey? apiKey;
  ///地图类型
  final MapType mapType;
  ///需要应用到地图上的手势集合
  final Set<Factory<OneSequenceGestureRecognizer>> gestureRecognizers;
  /// 初始化时的地图中心点
  final CameraPosition initialCameraPosition;
  /// 地图创建成功的回调, 收到此回调之后才可以操作地图
  final MapCreatedCallback? onMapCreated;
  //相机视角移动回调
  final ArgumentCallback<CameraPosition>? onCameraMoveEnd;

  const BXMapWidget({
    Key? key,
    this.apiKey,
    this.mapType = MapType.normal,
    this.initialCameraPosition = const CameraPosition(target: LatLng(22.548515, 114.066112), zoom: 10),
    this.gestureRecognizers = const <Factory<OneSequenceGestureRecognizer>>{},
    this.onMapCreated,
    this.onCameraMoveEnd
  }): super(key: key);

  @override
  State<StatefulWidget> createState() => _MapState();
}

class _MapState extends State<BXMapWidget> {
  final Completer<BXMapController> _controller = Completer<BXMapController>();
  late _BXMapOptions _mapOptions;

  @override
  Widget build(BuildContext context) {
    BXMapUtil.init(context);
    final Map<String, dynamic> creationsParams = <String, dynamic>{
      'apiKey': widget.apiKey?.toMap(),
      'initialCameraPosition': widget.initialCameraPosition.toMap(),
      'options': _mapOptions.toMap(),
    };
    Widget mapView = _methodChannel.buildView(
        creationsParams,
        widget.gestureRecognizers,
        onPlatformViewCreated,
    );
    return mapView;
  }

  @override
  void initState() {
    super.initState();
    _mapOptions = _BXMapOptions.fromWidget(widget);
    print('initState BXMapWidget');
  }

  Future<void> onPlatformViewCreated(int id) async {
    final BXMapController controller = await BXMapController.init(
      id,
      widget.initialCameraPosition,
      this,
    );
    _controller.complete(controller);
    final MapCreatedCallback? _onMapCreated = widget.onMapCreated;
    if (_onMapCreated != null) {
      _onMapCreated(controller);
    }
  }
}

///腾讯地图参数设置
class _BXMapOptions {
  /// 地图类型
  final MapType mapType;

  _BXMapOptions({
    this.mapType = MapType.normal,
  });

  static _BXMapOptions fromWidget(BXMapWidget map) {
    return _BXMapOptions(
      mapType: map.mapType,
    );
  }

  Map<String, dynamic> toMap() {
    final Map<String, dynamic> optionsMap = <String, dynamic>{};
    void addIfnonNull(String fieldName, dynamic value) {
      if (value != null) {
        optionsMap[fieldName] = value;
      }
    }
    addIfnonNull('mapType', mapType.index);
    return optionsMap;
  }

  Map<String, dynamic> _updatesMap(_BXMapOptions newOptions) {
    final Map<String, dynamic> preOptionsMap = toMap();
    return newOptions.toMap()
        ..removeWhere((String key, dynamic value) => (_checkChange(key, preOptionsMap[key], value)));
  }

  bool _checkChange(String key, dynamic preValue, dynamic newValue) {
    if (key == 'myLocationStyle' || key == 'customStyleOptions') {
      return preValue.toString() == newValue.toString();
    } else {
      return preValue == newValue;
    }
  }
}