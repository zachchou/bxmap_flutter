//
//  BXMapViewController.m
//  bxmap_flutter
//
//  Created by kangk on 2022/2/9.
//

#import "BXMapViewController.h"
#import "FlutterMethodChannel+MethodCallDispatch.h"
#import "BXMapJsonUtils.h"
#import "BXMapConvertUtil.h"
#import "BXMapCameraPosition.h"
#import <QMapKit/QMSSearchServices.h>
#import <QMapKit/QMSSearcher.h>

// 8.重新定义系统的NSLog，__OPTIMIZE__ 是release 默认会加的宏
#ifndef __OPTIMIZE__
#define NSLog(...) NSLog(__VA_ARGS__)
#else
#define NSLog(...) {}
#endif
static const int CENTER_IV_WIDTH = 48;

@interface BXMapViewController ()<QMapViewDelegate, QMSSearchDelegate>

@property(nonatomic, strong) QMapView *mapView;
@property(nonatomic, strong) QMSSearcher *mapSearcher;
@property(nonatomic, strong) FlutterMethodChannel *channel;
@property(nonatomic, strong) UIImageView *centerIV;
@property(nonatomic, strong) UIImageView *backIV;
@property(nonatomic, assign) int64_t viewId;
@property(nonatomic, strong) NSObject<FlutterPluginRegistrar> *registrar;

@property(nonatomic,copy) FlutterResult waitForMapCallBack; // waitForMap的回调，仅当地图没有加载完成时缓存使用
@property(nonatomic, assign) BOOL mapInitComleted; // 地图初始化完成，首帧回调的标记
@property(nonatomic, assign) BOOL isLocation;
@property(nonatomic, strong) BXMapCameraPosition *cameraPosition;

@property(nonatomic, assign) QMapRect initLimitMapRect; // 初始化时，限制地图适用范围；如果{0,0,0,0},则没有限制

@end

@implementation BXMapViewController
- (instancetype)initWithFrame:(CGRect)frame viewIdentifier:(int64_t)viewId arguments:(id)args registrar:(NSObject<FlutterPluginRegistrar> *)registrar {
    if (self = [super init]) {
        NSAssert([args isKindOfClass:[NSDictionary class]], @"传参错误");
        // 构建methodChannel
        NSString *channelName = [NSString stringWithFormat:@"bxmap_flutter_map_%lld", viewId];
        _channel = [FlutterMethodChannel methodChannelWithName:channelName binaryMessenger:registrar.messenger];
        
        NSDictionary *dict = args;
        
        NSDictionary *apiKey = dict[@"apiKey"];
        if (apiKey && [apiKey isKindOfClass:[NSDictionary class]]) {
            NSString *iosKey = apiKey[@"iosKey"];
            if (iosKey && iosKey.length > 0) {
                [QMapServices sharedServices].APIKey = iosKey;
                [QMSSearchServices sharedServices].apiKey = iosKey;
            }
        }
        // 统一检查key的设置是否生效
        NSAssert([QMapServices sharedServices].APIKey != nil, @"没有设置APIKey, 请先设置key");
        
        NSDictionary *cameraDict = [dict objectForKey:@"initialCameraPosition"];
        _cameraPosition = [BXMapJsonUtils modelFromDict:cameraDict modelClass:[BXMapCameraPosition class]];
        
        _viewId = viewId;
        
        self.mapInitComleted = NO;
        _mapView = [[QMapView alloc] initWithFrame:frame];
        _mapView.delegate = self;
        _mapView.accessibilityElementsHidden = NO;
        _mapView.showsCompass = NO; // 是否显示指南针
        _mapView.zoomLevel = _cameraPosition.zoom;
        [_mapView setCenterCoordinate:CLLocationCoordinate2DMake(_cameraPosition.target.latitude, _cameraPosition.target.longitude) animated:YES];
        
        _centerIV = [[UIImageView alloc] init];
        _centerIV.backgroundColor = [UIColor clearColor];
        _centerIV.frame = CGRectMake(0, 0, CENTER_IV_WIDTH, CENTER_IV_WIDTH);
//        _centerIV.layer.opacity = 0.2;
        [_mapView addSubview:_centerIV];
        
        _backIV = [[UIImageView alloc] init];
        _backIV.backgroundColor = [UIColor clearColor];
        _backIV.userInteractionEnabled = YES;
        _backIV.frame = CGRectMake(0, 0, CENTER_IV_WIDTH, CENTER_IV_WIDTH);
//        _centerIV.layer.opacity = 0.2;
        [_mapView addSubview:_backIV];
        
        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(backLocationTap)];
        [_backIV addGestureRecognizer:tap];
        
//        [_mapView setCenterCoordinate:cameraPosition.target animated:YES];
//        QPointAnnotation *point = [[QPointAnnotation alloc] init];
//        point.coordinate = cameraPosition.target;
//        [_mapView addAnnotation:point];
        _registrar = registrar;
        
//        _mapSearcher = [[QMSSearcher alloc] initWithDelegate:self];
//        QMSPoiSearchOption *poiSearchOption = [[QMSPoiSearchOption alloc] init];
//        poiSearchOption.boundary = @"nearby(40.040415,116.273511,500,0)";
//        [_mapSearcher searchWithPoiSearchOption:poiSearchOption];
        [self setMethodCallHandler];
        
        [_mapView setShowsUserLocation:YES];
    }
    return self;
}
- (void)backLocationTap {
    [_mapView setCenterCoordinate:_mapView.userLocation.location.coordinate animated:YES];

    BXMapCameraPosition *cameraPo = [[BXMapCameraPosition alloc] init];
    cameraPo.target = _mapView.userLocation.location.coordinate;
    NSDictionary *dict = [cameraPo toDictionary];
    [_channel invokeMethod:@"camera#onMoveEnd" arguments:@{@"position":dict}];
}
- (nonnull UIView *)view {
    return _mapView;
}
- (void)dealloc {
    if (QMapRectIsEmpty(_initLimitMapRect) == NO) {
        [_mapView removeObserver:self forKeyPath:@"frame"];
    }
}
- (void)setMethodCallHandler {
    __weak __typeof__(self) weakself = self;
    
    [self.channel addMethodName:@"map#waitForMap" withHandler:^(FlutterMethodCall * _Nonnull call, FlutterResult  _Nonnull result) {
            if (weakself.mapInitComleted) {
                result(nil);
            } else {
                weakself.waitForMapCallBack = result;
            }
    }];
    
    [self.channel addMethodName:@"map#cameraChange" withHandler:^(FlutterMethodCall * _Nonnull call, FlutterResult  _Nonnull result) {
        NSLog(@"call.argument: %@", call.arguments[@"position"][@"target"]);
        NSArray *array = call.arguments[@"position"][@"target"];
        [weakself.mapView setCenterCoordinate:CLLocationCoordinate2DMake([array[0] doubleValue], [array[1] doubleValue]) animated:YES];
    }];
}

#pragma mark - QMapViewDelegate

/**
 * @brief  在地图View将要启动定位时，会调用此函数
 * @param mapView 地图View
 */
- (void)mapViewWillStartLocatingUser:(QMapView *)mapView {
    NSLog(@"%s,mapView:%@",__func__,mapView);
    
    // 创建定位样式对象
    QUserLocationPresentation *presentation = [[QUserLocationPresentation alloc] init];
    // 定位图标
    NSString *key2 = [_registrar lookupKeyForAsset:@"packages/bxmap_flutter/images/mine_location.png"];
    UIImage *tmpImage = [UIImage imageNamed:key2];
    presentation.icon = [self image:tmpImage byScalingToSize:CGSizeMake(40, 40)];
    // 精度圈颜色
    presentation.circleFillColor = [UIColor clearColor];
    // 配置定位样式
    [_mapView configureUserLocationPresentation:presentation];
}

/**
 * @brief  在地图View停止定位后，会调用此函数
 * @param mapView 地图View
 */
- (void)mapViewDidStopLocatingUser:(QMapView *)mapView {
    NSLog(@"%s,mapView:%@",__func__,mapView);
}

/**
 * @brief 用户位置更新后，会调用此函数
 * @param mapView 地图View
 * @param userLocation 新的用户位置
 * @param fromHeading 是否为heading 变化触发，如果为location变化触发,则为NO
 */
- (void)mapView:(QMapView *)mapView didUpdateUserLocation:(QUserLocation *)userLocation fromHeading:(BOOL)fromHeading {
    NSLog(@"%s,mapView:%@ QUserLocation: %@",__func__,mapView, userLocation);
    NSLog(@"userLocation: %f----%f", _mapView.userLocation.location.coordinate.latitude, _mapView.userLocation.location.coordinate.longitude);
    if (!_isLocation && !_cameraPosition) {
        CLLocationCoordinate2D userL = CLLocationCoordinate2DMake(userLocation.location.coordinate.latitude, userLocation.location.coordinate.longitude);
        [_mapView setCenterCoordinate:userL];
        _isLocation = true;
    }
}
/**
 * @brief  定位失败后，会调用此函数
 * @param mapView 地图View
 * @param error 错误号，参考CLError.h中定义的错误号
 */
- (void)mapView:(QMapView *)mapView didFailToLocateUserWithError:(NSError *)error {
    NSLog(@"%s,mapView:%@ error:%@",__func__,mapView,error);
}
/**
 *  @brief  地图数据加载失败时会调用此接口
 *
 *  @param mapView  地图view
 *  @param error 错误信息
 */
- (void)mapViewDidFailLoadingMap:(QMapView *)mapView withError:(NSError *)error {
    NSLog(@"%s,mapView:%@ error:%@",__func__,mapView,error);
}
- (void)mapViewInitComplete:(QMapView *)mapView {
    NSLog(@"%s,mapView:%@", __func__, mapView);
    self.mapInitComleted = YES;
    if (self.waitForMapCallBack) {
        self.waitForMapCallBack(nil);
        self.waitForMapCallBack = nil;
    }
    
}
- (QAnnotationView *)mapView:(QMapView *)mapView viewForAnnotation:(id<QAnnotation>)annotation {
    if ([annotation isKindOfClass:[QPointAnnotation class]]) {
        static NSString *identifier = @"pointAnnotation";
        QPinAnnotationView *annotationView = (QPinAnnotationView *)[mapView dequeueReusableAnnotationViewWithIdentifier:identifier];
        if (annotationView == nil) {
            annotationView = [[QPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:identifier];
            annotationView.canShowCallout = YES;
            annotationView.pinColor = QPinAnnotationColorRed;
        }
        return  annotationView;
    }
    return  nil;
}

/**
 * @brief  地图区域即将改变时会调用此接口
 * @param mapView 地图View
 * @param animated 是否动画
 * @param bGesture 是否由手势触发
 */
- (void)mapView:(QMapView *)mapView regionWillChangeAnimated:(BOOL)animated gesture:(BOOL)bGesture {
    NSLog(@"%s,mapView:%@ ",__func__,mapView);
    NSLog(@"longitude: %f---latitude: %f",mapView.centerCoordinate.longitude, mapView.centerCoordinate.latitude);
}

/**
 * @brief  地图区域改变时会调用此接口. 视野持续变化时本回调可能会被频繁多次调用, 请不要做耗时或复杂的事情
 * @param mapView 地图View
 */
- (void)mapViewRegionChange:(QMapView *)mapView {
    NSLog(@"%s,mapView:%@ ",__func__,mapView);
    NSLog(@"longitude: %f---latitude: %f",mapView.centerCoordinate.longitude, mapView.centerCoordinate.latitude);
}

/**
 * @brief  地图区域改变完成后会调用此接口,如果是由手势触发，当触摸结束且地图region改变的动画结束后才会触发此回调
 * @param mapView 地图View
 * @param animated 是否动画
 * @param bGesture region变化是否由手势触发
 */
- (void)mapView:(QMapView *)mapView regionDidChangeAnimated:(BOOL)animated gesture:(BOOL)bGesture {
    NSLog(@"%s,mapView:%@ ",__func__,mapView);
    NSLog(@"longitude: %f---latitude: %f",mapView.centerCoordinate.longitude, mapView.centerCoordinate.latitude);
    NSLog(@"bGesture: %@", @(bGesture));
    if (bGesture) {
        BXMapCameraPosition *cameraPo = [[BXMapCameraPosition alloc] init];
        cameraPo.target = mapView.centerCoordinate;
        NSDictionary *dict = [cameraPo toDictionary];
        [_channel invokeMethod:@"camera#onMoveEnd" arguments:@{@"position":dict}];
    }
}

/**
 * @brief 地图渲染每一帧过程中都会调用此接口
 * @param mapView 地图View
 */
- (void)mapViewDrawFrame:(QMapView *)mapView {
//    NSLog(@"%s,mapView:%@ ",__func__,mapView);
//    NSLog(@"longitude: %f---latitude: %f",mapView.centerCoordinate.longitude, mapView.centerCoordinate.latitude);
    if (!self.mapInitComleted) {
        [_mapView setLogoMargin:CGPointMake(6, 3) anchor:QMapLogoAnchorLeftBottom];
        [_mapView setLogoScale:0.6];
        _centerIV.frame = CGRectMake(mapView.bounds.size.width/2.0-CENTER_IV_WIDTH/2.0, mapView.bounds.size.height/2.0-CENTER_IV_WIDTH/2.0-25, CENTER_IV_WIDTH, CENTER_IV_WIDTH);
        NSString *key = [_registrar lookupKeyForAsset:@"packages/bxmap_flutter/images/center_location.png"];
        _centerIV.image = [UIImage imageNamed:key];
        
        _backIV.frame = CGRectMake(mapView.bounds.size.width-CENTER_IV_WIDTH-10, mapView.bounds.size.height-CENTER_IV_WIDTH-10, CENTER_IV_WIDTH, CENTER_IV_WIDTH);
        NSString *key1 = [_registrar lookupKeyForAsset:@"packages/bxmap_flutter/images/back_location.png"];
        _backIV.image = [UIImage imageNamed:key1];
    }
}

/**
 * @brief  点击地图空白处会调用此接口.
 * @param mapView 地图View
 * @param coordinate 坐标
 */
- (void)mapView:(QMapView *)mapView didTapAtCoordinate:(CLLocationCoordinate2D)coordinate {
    NSLog(@"%s,mapView:%@ ",__func__,mapView);
    NSLog(@"longitude: %f---latitude: %f",mapView.centerCoordinate.longitude, mapView.centerCoordinate.latitude);
}

/**
 * @brief  点击地图poi图标处会调用此接口.
 * @param mapView 地图View
 * @param poi poi数据
 */
- (void)mapView:(QMapView *)mapView didTapPoi:(QPoiInfo *)poi {
    NSLog(@"%s,mapView:%@ ",__func__,mapView);
    NSLog(@"longitude: %f---latitude: %f",mapView.centerCoordinate.longitude, mapView.centerCoordinate.latitude);
    NSLog(@"poi: %@", poi.name);
    BXMapCameraPosition *cameraPo = [[BXMapCameraPosition alloc] init];
    cameraPo.target = poi.coordinate;
    NSDictionary *dict = [cameraPo toDictionary];
    [_channel invokeMethod:@"map#onPoiTouched" arguments:@{@"position": dict}];
    [mapView setCenterCoordinate:poi.coordinate animated:YES];
}

#pragma mark - QMSSearchDelegate
/*!
 *  @brief  查询出现错误
 *
 *  @param searchOption 查询参数
 *  @param error        错误
 */
- (void)searchWithSearchOption:(QMSSearchOption *)searchOption didFailWithError:(NSError*)error {
    NSLog(@"error: %@", error.debugDescription);
}
/*!
 *  @brief  poi查询结果回调函数
 *
 *  @param poiSearchOption 查询参数
 *  @param poiSearchResult 查询结果
 */
- (void)searchWithPoiSearchOption:(QMSPoiSearchOption *)poiSearchOption didReceiveResult:(QMSPoiSearchResult *)poiSearchResult {
    NSLog(@"QMSPoiSearchOption: %@", poiSearchOption);
    NSLog(@"QMSPoiSearchResult: %@", poiSearchResult);
}

/*!
 *  @brief  地址解析(地址转坐标)结果回调接口
 *
 *  @param geoCodeSearchOption 查询参数
 *  @param geoCodeSearchResult 查询结果
 */
- (void)searchWithGeoCodeSearchOption:(QMSGeoCodeSearchOption *)geoCodeSearchOption didReceiveResult:(QMSGeoCodeSearchResult *)geoCodeSearchResult {
    NSLog(@"QMSGeoCodeSearchResult: %@", geoCodeSearchResult);
}

- (UIImage *)image:(UIImage*)image byScalingToSize:(CGSize)targetSize {
   UIImage *sourceImage = image;
   UIImage *newImage = nil;

   UIGraphicsBeginImageContext(targetSize);

   CGRect thumbnailRect = CGRectZero;
   thumbnailRect.origin = CGPointZero;
   thumbnailRect.size.width  = targetSize.width;
   thumbnailRect.size.height = targetSize.height;

   [sourceImage drawInRect:thumbnailRect];

   newImage = UIGraphicsGetImageFromCurrentImageContext();
   UIGraphicsEndImageContext();

   return newImage ;
}
@end
