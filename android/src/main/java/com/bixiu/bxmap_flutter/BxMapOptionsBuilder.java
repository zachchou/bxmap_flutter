package com.bixiu.bxmap_flutter;

import android.content.Context;

import com.bixiu.bxmap_flutter.core.BxMapOptionsSink;
import com.bixiu.bxmap_flutter.utils.LogUtil;
import com.tencent.tencentmap.mapsdk.maps.TencentMapOptions;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;

public class BxMapOptionsBuilder implements BxMapOptionsSink {
    private static final String CLASS_NAME = "BxMapOptionsBuilder";
    private final TencentMapOptions options = new TencentMapOptions();
//    private BxMapPlatformView bxMapPlatformView;
    private CameraPosition cameraPosition;
    private float minZoomLevel = 3;
    private float maxZoomLevel = 20;

    BxMapPlatformView build(int id,
                            Context context,
                            FlutterPlugin.FlutterAssets flutterAssets,
                            BinaryMessenger binaryMessenger,
                            LifecycleProvider lifecycleProvider, FlutterPlugin.FlutterPluginBinding binding) {
        try {
            final BxMapPlatformView bxMapPlatformView = new BxMapPlatformView(id, context, flutterAssets, binaryMessenger, lifecycleProvider, options, cameraPosition, binding);
            return  bxMapPlatformView;
        } catch (Throwable e) {
            LogUtil.e(CLASS_NAME, "build", e);
        }
        return  null;
    }

    @Override
    public void setCamera(CameraPosition cameraPosition) {
        this.cameraPosition = cameraPosition;
    }
}
