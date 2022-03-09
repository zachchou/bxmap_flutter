//
//  BXMapMethodCallDispatcher.h
//  bxmap_flutter
//
//  Created by kangk on 2022/2/11.
//

#import <Foundation/Foundation.h>
#import <Flutter/Flutter.h>

NS_ASSUME_NONNULL_BEGIN

/// methodCall的分发器
@interface BXMapMethodCallDispatcher : NSObject

- (void)onMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result;

- (void)addMethodName:(NSString *)methodName withHandler:(FlutterMethodCallHandler)handler;

- (void)removeHandlerWithMethodName:(NSString *)methodName;

- (void)clearAllHandler;

@end

NS_ASSUME_NONNULL_END
