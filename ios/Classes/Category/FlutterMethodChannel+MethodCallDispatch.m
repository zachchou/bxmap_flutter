//
//  FlutterMethodChannel+MethodCallDispatch.m
//  bxmap_flutter
//
//  Created by kangk on 2022/2/11.
//

#import "FlutterMethodChannel+MethodCallDispatch.h"
#import "objc/runtime.h"

@implementation FlutterMethodChannel (MethodCallDispatch)

- (BXMapMethodCallDispatcher *)methodCallDispatcher {
    return objc_getAssociatedObject(self, @selector(methodCallDispatcher));
}
- (void)setMethodCallDispatcher:(BXMapMethodCallDispatcher * _Nonnull)dispatcher {
    objc_setAssociatedObject(self, @selector(methodCallDispatcher), dispatcher, OBJC_ASSOCIATION_RETAIN);
}
- (void)addMethodName:(NSString *)methodName withHandler:(FlutterMethodCallHandler)handler {
    if (self.methodCallDispatcher == nil) {
        self.methodCallDispatcher = [[BXMapMethodCallDispatcher alloc] init];
        __weak typeof(self) weakSelf = self;
        [self setMethodCallHandler:^(FlutterMethodCall * _Nonnull call, FlutterResult _Nonnull result) {
            if (weakSelf.methodCallDispatcher) {
                [weakSelf.methodCallDispatcher onMethodCall:call result: result];
            }
        }];
    }
    [self.methodCallDispatcher addMethodName:methodName withHandler:handler];
}

- (void)removeHandlerWithMethodName:(NSString *)methodName {
    [self.methodCallDispatcher removeHandlerWithMethodName:methodName];
}

- (void)clearAllHandler {
    [self.methodCallDispatcher clearAllHandler];
}
@end
