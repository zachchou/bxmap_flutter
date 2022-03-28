package com.bixiu.bxmap_flutter;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.bixiu.bxmap_flutter.utils.LogUtil;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.embedding.engine.plugins.lifecycle.FlutterLifecycleAdapter;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;

/** BxmapFlutterPlugin */
public class BxmapFlutterPlugin implements FlutterPlugin, ActivityAware {
  private static final String CLASS_NAME = "BxmapFlutterPlugin";
  private FlutterPluginBinding pluginBinding;
  private Lifecycle lifecycle;

  private static  final String VIEW_TYPE = "com.bixiu.bxmap_flutter";

//  public static void registerWith(PluginRegistry.Registrar registrar) {
//    LogUtil.i(CLASS_NAME, "registerWith====>");
//
//    final Activity activity = registrar.activity();
//    if (activity == null) {
//      LogUtil.w(CLASS_NAME, "activity is null!");
//      return;
//    }
//    if (activity instanceof LifecycleOwner) {
//      registrar.platformViewRegistry().registerViewFactory(
//              VIEW_TYPE,
//              new BxMapPlatformViewFactory(
//                      registrar.messenger(),
//                      new LifecycleProvider() {
//                        @Override
//                        public Lifecycle getLifecycle() {
//                          return ((LifecycleOwner) activity).getLifecycle();
//                        }
//                      }));
//    } else {
//      registrar.platformViewRegistry().registerViewFactory(
//              VIEW_TYPE,
//              new BxMapPlatformViewFactory(registrar.messenger(), new ProxyLifecycleProvider(activity)));
//    }
//  }

  public BxmapFlutterPlugin() {}

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
    LogUtil.i(CLASS_NAME, "onAttachedToEngine==>");
    pluginBinding = binding;
    binding
            .getPlatformViewRegistry()
            .registerViewFactory(
                    VIEW_TYPE,
                    new BxMapPlatformViewFactory(
                            binding.getBinaryMessenger(),
                            new LifecycleProvider() {
                              @Nullable
                              @Override
                              public Lifecycle getLifecycle() {
                                return lifecycle;
                              }
                            }, binding.getFlutterAssets(), binding));
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    LogUtil.i(CLASS_NAME, "onDetachedFromEngine==>");
    pluginBinding = null;
  }


  // ActivityAware

  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    LogUtil.i(CLASS_NAME, "onAttachedToActivity==>");
    lifecycle = FlutterLifecycleAdapter.getActivityLifecycle(binding);
  }

  @Override
  public void onDetachedFromActivity() {
    LogUtil.i(CLASS_NAME, "onDetachedFromActivity==>");
    lifecycle = null;
  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
    LogUtil.i(CLASS_NAME, "onReattachedToActivityForConfigChanges==>");
    onAttachedToActivity(binding);
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {
    LogUtil.i(CLASS_NAME, "onDetachedFromActivityForConfigChanges==>");
    this.onDetachedFromActivity();
  }
}
