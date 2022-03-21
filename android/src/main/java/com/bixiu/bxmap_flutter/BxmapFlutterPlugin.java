package com.bixiu.bxmap_flutter;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.bixiu.bxmap_flutter.utils.LogUtil;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
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

  public static void registerWith(PluginRegistry.Registrar registrar) {
    LogUtil.i(CLASS_NAME, "registerWith====>");

    final Activity activity = registrar.activity();
    if (activity == null) {
      LogUtil.w(CLASS_NAME, "activity is null!");
      return;
    }
    if (activity instanceof LifecycleOwner) {

    } else {

    }
  }

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
  }

  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {

  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {

  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {

  }

  @Override
  public void onDetachedFromActivity() {

  }
}
