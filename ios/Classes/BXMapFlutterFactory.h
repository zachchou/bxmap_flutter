//
//  BXMapFlutterFactory.h
//  bxmap_flutter
//
//  Created by kangk on 2022/2/9.
//

#import <Foundation/Foundation.h>
#import <Flutter/Flutter.h>

NS_ASSUME_NONNULL_BEGIN

@interface BXMapFlutterFactory : NSObject<FlutterPlatformViewFactory>
- (instancetype)initWithRegistrar:(NSObject<FlutterPluginRegistrar> *)registrar;
@end

NS_ASSUME_NONNULL_END
