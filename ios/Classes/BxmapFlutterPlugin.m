#import "BxmapFlutterPlugin.h"
#import "BXMapFlutterFactory.h"

@implementation BxmapFlutterPlugin{
    NSObject<FlutterPluginRegistry> *_registrar;
    FlutterMethodChannel *_channel;
    NSMutableDictionary *_mapControllers;
    
}
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
    BXMapFlutterFactory *bxMapFactory = [[BXMapFlutterFactory alloc] initWithRegistrar:registrar];
    [registrar registerViewFactory:bxMapFactory
                            withId:@"com.bixiu.bxmap_flutter"
                gestureRecognizersBlockingPolicy:FlutterPlatformViewGestureRecognizersBlockingPolicyWaitUntilTouchesEnded];

//  FlutterMethodChannel* channel = [FlutterMethodChannel
//      methodChannelWithName:@"bxmap_flutter"
//            binaryMessenger:[registrar messenger]];
//  BxmapFlutterPlugin* instance = [[BxmapFlutterPlugin alloc] init];
//  [registrar addMethodCallDelegate:instance channel:channel];
}

//- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
//  if ([@"getPlatformVersion" isEqualToString:call.method]) {
//    result([@"iOS " stringByAppendingString:[[UIDevice currentDevice] systemVersion]]);
//  } else {
//    result(FlutterMethodNotImplemented);
//  }
//}

@end
