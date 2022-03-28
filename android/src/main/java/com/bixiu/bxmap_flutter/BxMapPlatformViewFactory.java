package com.bixiu.bxmap_flutter;

import android.content.Context;

import com.bixiu.bxmap_flutter.utils.ConvertUtil;
import com.bixiu.bxmap_flutter.utils.LogUtil;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;

import java.util.Map;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;

public class BxMapPlatformViewFactory extends PlatformViewFactory {
    private static final String CLASS_NAME = "BxMapPlatformViewFactory";
    private final BinaryMessenger binaryMessenger;
    private final LifecycleProvider lifecycleProvider;
    private final FlutterPlugin.FlutterAssets flutterAssets;
    private final FlutterPlugin.FlutterPluginBinding binding;

    BxMapPlatformViewFactory(BinaryMessenger binaryMessenger, LifecycleProvider lifecycleProvider,
                             FlutterPlugin.FlutterAssets flutterAssets, FlutterPlugin.FlutterPluginBinding binding) {
        super(StandardMessageCodec.INSTANCE);
        this.binaryMessenger = binaryMessenger;
        this.lifecycleProvider = lifecycleProvider;
        this.flutterAssets = flutterAssets;
        this.binding = binding;
    }

    @Override
    public PlatformView create(Context context, int viewId, Object args) {
        final BxMapOptionsBuilder builder = new BxMapOptionsBuilder();
        Map<String, Object> params = null;
        try {
            ConvertUtil.density = context.getResources().getDisplayMetrics().density;
            params = (Map<String, Object>) args;
            LogUtil.i(CLASS_NAME,"create params==>" + params);
            Object options = ((Map<String, Object>) args).get("options");
            if (options != null) {
                ConvertUtil.interpretBxMapOptions(options, builder);
            }

            if (params.containsKey("initialCameraPosition")) {
                CameraPosition cameraPosition = ConvertUtil.toCameraPosition(params.get("initialCameraPosition"));
                builder.setCamera(cameraPosition);
            }

//            if (params.containsKey("markersToAdd")) {
//                builder.setInitialMarkers(params.get("markersToAdd"));
//            }
//            if (params.containsKey("polylinesToAdd")) {
//                builder.setInitialPolylines(params.get("polylinesToAdd"));
//            }
//
//            if (params.containsKey("polygonsToAdd")) {
//                builder.setInitialPolygons(params.get("polygonsToAdd"));
//            }


            if (params.containsKey("apiKey")) {
                ConvertUtil.checkApiKey(params.get("apiKey"));
            }

            if (params.containsKey("debugMode")) {
                LogUtil.isDebugModel = ConvertUtil.toBoolean(params.get("debugMode"));
            }

        } catch (Throwable e) {
            LogUtil.e(CLASS_NAME, "create", e);
        }
        return builder.build(viewId, context, flutterAssets, binaryMessenger, lifecycleProvider, binding);
    }
}
