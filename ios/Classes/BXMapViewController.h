//
//  BXMapViewController.h
//  bxmap_flutter
//
//  Created by kangk on 2022/2/9.
//

#import <Foundation/Foundation.h>
#import <Flutter/Flutter.h>
#import <QMapKit/QMapKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface BXMapViewController : NSObject<FlutterPlatformView>
- (instancetype)initWithFrame:(CGRect)frame
               viewIdentifier:(int64_t)viewId
                    arguments:(id _Nullable)args
                    registrar:(NSObject<FlutterPluginRegistrar>*)registrar;
@end

NS_ASSUME_NONNULL_END
