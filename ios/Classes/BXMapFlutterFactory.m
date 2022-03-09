//
//  BXMapFlutterFactory.m
//  bxmap_flutter
//
//  Created by kangk on 2022/2/9.
//

#import "BXMapFlutterFactory.h"
#import "BXMapViewController.h"

@implementation BXMapFlutterFactory {
    NSObject<FlutterPluginRegistrar> *_registrar;
}

- (instancetype)initWithRegistrar:(NSObject<FlutterPluginRegistrar> *)registrar {
    self = [super init];
    if (self) {
        _registrar = registrar;
    }
    return self;
}
- (NSObject<FlutterMessageCodec> *)createArgsCodec {
    return [FlutterStandardMessageCodec sharedInstance];
}
- (nonnull NSObject<FlutterPlatformView> *)createWithFrame:(CGRect)frame viewIdentifier:(int64_t)viewId arguments:(id _Nullable)args {
    return [[BXMapViewController alloc] initWithFrame:frame viewIdentifier:viewId arguments:args registrar:_registrar];
}

@end
